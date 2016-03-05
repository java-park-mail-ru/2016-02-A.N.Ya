from urllib2 import Request, urlopen
from sys import argv

adress = "http://localhost:8080/api/session"
values = ""

if len(argv) > 2:
    values += "{ \"login\": \""
    values += argv[1]
    values += "\", \"password\": \""
    values += argv[2]
    values += "\" }"
else:
    values = """
      {
        "login": "mamba",
        "password": "test"
      }
    """

print values

headers = {
    'Content-Type': 'application/json'
}
request = Request(adress, data=values, headers=headers)
request.get_method = lambda: 'PUT'

response_body = urlopen(request).read()
print response_body