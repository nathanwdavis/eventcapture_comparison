Simple test I did:

`ab -n 1000 -c 50 -T application/x-www-form-urlencoded -p benchmark/sample_post_data.txt -k http://localhost:3000/capture`

(TRUNCATEd and VACUUM ANALYZEd the eventlog table before each run)

Clojure result:
```
Time taken for tests:   0.750 seconds
Complete requests:      1000
Failed requests:        0
Requests per second:    1334.06 [#/sec] (mean)

Percentage of the requests served within a certain time (ms)
  50%     20
  66%     27
  75%     36
  80%     46
  90%     82
  95%    123
  98%    200
  99%    249
 100%    321 (longest request)
```

Golang result:
```
Time taken for tests:   0.171 seconds
Complete requests:      1000
Failed requests:        0
Requests per second:    5845.49 [#/sec] (mean)

Percentage of the requests served within a certain time (ms)
  50%      8
  66%      9
  75%     10
  80%     11
  90%     12
  95%     13
  98%     14
  99%     14
 100%     17 (longest request)
```
