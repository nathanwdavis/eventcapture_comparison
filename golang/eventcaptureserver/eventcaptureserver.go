package main

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"github.com/codegangsta/martini"
	_ "github.com/lib/pq"
	"net/http"
)

var (
	Db *sql.DB
)

func routes() martini.Router {
	r := martini.NewRouter()
	r.Get("/", func() string {
		return "It works!"
	})
	r.Post("/capture", handleCapture)
	return r
}

func handleCapture(req *http.Request, params martini.Params) (string, int) {
	event_type := req.PostFormValue("event_type")
	data := req.PostFormValue("data")
	var dataAsMap map[string]interface{}
	if err := json.Unmarshal(
		[]byte(data), &dataAsMap); err != nil ||
		len(event_type) == 0 {

		return "Invalid input params", 400
	}
	event := &Event{event_type, req.PostFormValue("ext_ref"),
		req.PostFormValue("user_ref"), data}
	if err := event.Save(); err != nil {
		fmt.Println(err)
		return "Event capture failed", 500
	}
	return "", 204
}

type Event struct {
	event_type, ext_ref, user_ref, data string
}

func (e *Event) Save() error {
	row := Db.QueryRow(`select insert_eventlog($1, $2, $3, $4)`,
		e.event_type,
		e.ext_ref,
		e.user_ref,
		e.data)
	if err := row.Scan(new(int)); err != nil {
		return err
	}
	return nil
}

func dbSetup() *sql.DB {
	db, err := sql.Open("postgres",
		"postgres://eventcaptureuser:postgres@localhost/event?sslmode=disable")
	if err != nil {
		panic(err)
	}
	return db
}

func main() {
	Db = dbSetup()

	m := martini.New()
	//m.Use(martini.Logger())
	m.Use(martini.Recovery())
	m.Action(routes().Handle)
	m.Run()
}
