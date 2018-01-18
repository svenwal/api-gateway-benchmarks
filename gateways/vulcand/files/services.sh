etcdctl set /vulcand/backends/b1/backend '{"Type": "http"}'
etcdctl set /vulcand/backends/b1/servers/srv1 '{"URL": "http://webserver:8888"}'
etcdctl set /vulcand/frontends/f1/frontend '{"Type": "http", "BackendId": "b1", "Route": "Path(`/test01`)"}'