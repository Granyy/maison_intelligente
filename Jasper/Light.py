#******************************************************************************#
#*        @TITRE : Light.py                                                   *#
#*      @VERSION : 1.0                                                        *#
#*     @CREATION : 05 01, 2017                                                *#
#* @MODIFICATION : 05 21, 2017                                                *#
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
import datetime
import struct
import urllib
import feedparser
import requests
import bs4
from client.app_utils import getTimezone
from semantic.dates import DateService


WORDS = ["LIGHT", "DOWN", "ON"]


def handle(text, mic, profile):

	targetUrl = profile['target']["IP_PORT"]
	targetMn = profile['target']["ID_MN"]
	targetAE = 'LED1'
	url = 'http://' + targetUrl + '/~/' + targetMn + '/mn-name/' + targetAE
	
	if re.search(r'\bDOWN\b',text,re.IGNORECASE):
		querystring = {"op":"ALLfalse"}
		sentence = "I have turned down the light, sir"
	else:
		querystring = {"op":"ALLtrue"}
		sentence = "I have turned on the light, sir"

	headers = {
		'x-m2m-origin': "admin:admin",
		'cache-control': "no-cache",
		'postman-token': "ace35016-5a65-4933-1d47-2effe202a215"
		}

	response = requests.request("POST", url, headers=headers, params=querystring)

	print(response.text)
	print sentence
	mic.say(sentence)


def isValid(text):
    return bool(re.search(r'\blight\b', text, re.IGNORECASE))
