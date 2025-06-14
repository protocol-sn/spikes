from flask import Flask, request
import requests

app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello World!'
    
@app.route('/request')
def offsite_hello():
	return requests.get('http://backend-two:5002').content
	
@app.route('/params/path/<param>')
def path_param(param):
	return "got path param " + param
	
@app.route('/params/query')
def query_param():
	return "got query param " + request.args.get('myQuery')
