package main

import (
	"github.com/codegangsta/martini"
)

func routes(app *martini.Martini) {

}

func main() {
	m := martini.Classic()
	m.Get("/", func() string {
		return "It works!"
	})
	m.Post("/capture", func(params martini.Params) string {
		return ""
	})
	m.Run()
}
