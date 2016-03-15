from urllib2 import Request, urlopen

values = ""

headers = {
    'Content-Type': 'application/json'
}

adress = "http://localhost:8080/api/session"

request = Request(adress, data=values, headers=headers)
request.get_method = lambda: 'DELETE'

response_body = urlopen(request).read()
print response_body