#******************************************************************************#
#*        @TITRE : OWeather.py                                                *#
#*      @VERSION : 1.0                                                        *#
#*     @CREATION : 05 21, 2017                                                *#
#* @MODIFICATION :                                                            *#
#*      @AUTEURS : Leo GRANIER                                                *#
#*    @COPYRIGHT : Copyright (c) 2017                                         *#
#*                 Paul GUIRBAL                                               *#
#*                 Joram FILLOL-CARLINI                                       *#
#*                 Gianni D'AMICO                                             *#
#*                 Matthieu BOUGEARD                                          *#
#*                 Leo GRANIER                                                *#
#*      @LICENSE : MIT License (MIT)                                          *#
#******************************************************************************#

import re
import urllib
import requests
import json
from semantic.numbers import NumberService

WORDS = ["WEATHER","FORECAST", "TODAY", "TOMORROW"]

def handle(text, mic, profile):
	
	if 'OpenWeatherMap' in profile:
		
		town = profile['OpenWeatherMap']["city_name"]
		api_key = profile['OpenWeatherMap']["api_key"]
		
		headers = {
				'cache-control': "no-cache",
				'postman-token': "f1e7fb6f-116d-7372-3c84-09fd0259ca53"
				}
		

		if re.search(r'\bTOMORROW\b',text,re.IGNORECASE):
			
			url = 'http://api.openweathermap.org/data/2.5/forecast?q=' + town + '&appid=' + api_key
			
			response = requests.request("GET", url, headers=headers)

			try:
				decoded = json.loads(response.text)
			
				print json.dumps(decoded, sort_keys=True, indent=4)
				
				main = decoded['list'][9]['weather'][0]['main']
				description = decoded['list'][9]['weather'][0]['description']
				temp = str(int(decoded['list'][9]['main']['temp']) - 273)
				pressure = decoded['list'][9]['main']['pressure']
				humidity = str(decoded['list'][9]['main']['humidity'])
				wind = str(int(decoded['list'][9]['wind']['speed']))
				
			except (ValueError, KeyError, TypeError):
				print "JSON format error"
			
			weather_report = "Weather for tomorrow at " + town + ". Tomorrow will be mainly " + main + ". There is a chance of "  \
					+ description +". Temperature will be "+ temp + " degree"  \
					". Humidity will be "+ humidity +" percent. Wind Speed will be "  \
					+ wind +" meter per second."
		
		else:
			
			url = 'http://api.openweathermap.org/data/2.5/weather?q=' + town + '&appid=' + api_key
			
			response = requests.request("GET", url, headers=headers)

			try:
				decoded = json.loads(response.text)
			
				print json.dumps(decoded, sort_keys=True, indent=4)
				
				main = decoded['weather'][0]['main']
				description = decoded['weather'][0]['description']
				temp = str(int(decoded['main']['temp']) - 273)
				pressure = decoded['main']['pressure']
				humidity = str(decoded['main']['humidity'])
				wind = str(int(decoded['wind']['speed']))
			
			except (ValueError, KeyError, TypeError):
				print "JSON format error"
			
			weather_report = "Weather for today at " + town + ". Today is mainly " + main + ". There is a chance of "  \
								+ description +". Now Temperature is "+ temp + " degree"  \
								". Humidity is "+ humidity +" percent. Wind Speed is "  \
								+ wind +" meter per second."
			
		
		mic.say(weather_report)
		
	else:
		mic.say("I am having some trouble accessing that information. PLease check your profile info.")
		
	
def isValid(text):
	return bool(re.search(r'\bweather\b', text, re.IGNORECASE))