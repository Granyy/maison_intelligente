#******************************************************************************#
#*        @TITRE : Temperature.py                                             *#
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
from xml.etree import ElementTree as ET
from client.app_utils import getTimezone
from semantic.dates import DateService


WORDS = ["TEMPERATURE"]


def handle(text, mic, profile):
	
	if 'target' in profile:

		targetUrl = profile['target']["IP_PORT"]
		targetMn = profile['target']["ID_MN"]
		targetAE = 'TEMPERATURE'
		url = 'http://' + targetUrl + '/~/' + targetMn + '/mn-name/' + targetAE

		querystring = {"op":"get"}

		headers = {
			'x-m2m-origin': "admin:admin",
			'cache-control': "no-cache",
			'postman-token': "ace35016-5a65-4933-1d47-2effe202a215"
			}

		response = requests.request("POST", url, headers=headers, params=querystring)
		
		print(response.text)
		
		tree = ET.fromstring(response.text)
		print("OK")
		node = tree.find('./real')
		print node.tag
		for name, value in sorted(node.attrib.items()):
			print '  %-4s = "%s"' % (name, value)
			if name=="val":
				mic.say("It is %s degree celsius right now." %value)
		
	else:
		mic.say("I am having some trouble accessing that information. PLease check your profile info.")


def isValid(text):
    return bool(re.search(r'\b(temperature)\b', text, re.IGNORECASE))
