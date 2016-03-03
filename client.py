from urllib2 import Request, urlopen
from urllib import urlencode

values = {
    "id": 7,
    "login": "admin",
    "password": "test",
    "email": "test"  }

data = urlencode(values)

headers = {
    'Content-Type': 'application/json'
}

request = Request('http://localhost:8080/api/user', data, headers)

response_body = urlopen(request).read()
print response_body
