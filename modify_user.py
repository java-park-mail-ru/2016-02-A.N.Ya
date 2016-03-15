from urllib2 import Request, urlopen
from sys import argv

adress = "http://localhost:8080/api/user/"
values = ""

if len(argv) > 4:
    adress += argv[1]
    values += "{ \"login\": \""
    values += argv[2]
    values += "\", \"password\": \""
    values += argv[3]
    values += "\", \"email\": \""
    values += argv[4]
    values += "\" }"
else:
    adress += '2'
    values = """
      {
        "login": "test",
        "password": "modyfied_test",
        "email": "test"
      }
    """

headers = {
    'Content-Type': 'application/json'
}
request = Request(adress, data=values, headers=headers)

response_body = urlopen(request).read()
print response_body