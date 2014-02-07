Simple test I did:

`ab -n 1000 -c 50 -T application/x-www-form-urlencoded -p benchmark/sample_post_data.txt http://localhost:3000/capture`

(TRUNCATEd the eventlog table before each run)

Clojure result:
```
Requests per second:    374.31 [#/sec] (mean)

Percentage of the requests served within a certain time (ms)
  50%    129
  66%    139
  75%    148
  80%    155
  90%    179
  95%    199
  98%    222
  99%    245
 100%    364 (longest request)
```

Golang result:
```
Requests per second:    1157.66 [#/sec] (mean)

Percentage of the requests served within a certain time (ms)
  50%     43
  66%     50
  75%     53
  80%     54
  90%     58
  95%     63
  98%     68
  99%     73
 100%     82 (longest request)
```
