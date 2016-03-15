from urllib2 import Request, urlopen
from sys import argv

values = ""

headers = {
    'Content-Type': 'application/json'
}

adress = "http://localhost:8080/api/user/"

if len(argv) > 1:
    adress += argv[1]


request = Request(adress, data=values, headers=headers)
request.get_method = lambda: 'DELETE'

response_body = urlopen(request).read()
print response_body