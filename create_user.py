from urllib2 import Request, urlopen
from sys import argv

values = ""

if len(argv) > 3:
    values += "{ \"login\": \""
    values += argv[1]
    values += "\", \"password\": \""
    values += argv[2]
    values += "\", \"email\": \""
    values += argv[3]
    values += "\" }"
else:
    values = """
      {
        "login": "mamba",
        "password": "test",
        "email": "test"
      }
    """

print values

headers = {
    'Content-Type': 'application/json'
}
request = Request('http://localhost:8080/api/user', data=values, headers=headers)
request.get_method = lambda: 'PUT'

response_body = urlopen(request).read()
print response_body