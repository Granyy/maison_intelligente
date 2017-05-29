var http = require('http'),
    express = require('express'),
    app = require('express')(),
    server = require('http').createServer(app),
    io = require('socket.io').listen(server),
    parseString = require('xml2js').parseString;

app.use(express.static(__dirname));

//Déclaration de variables utiles

var interval = 2000;
var profile ;
var tableProfiles = {
    'joram' : 'USER1',
    'paul' : 'USER2',
    'general' : 'GENERAL'
};
var param_user = {
    'home': 'value',
    'tempTh': 'value',
    'lumiTh': 'value',
    'LED2': 'value',
    'LED1': 'value',
    'LED_RGB' : {
        'red': 'value',
        'green': 'value',
        'blue': 'value',
    }
};

var param_manual = {
    'LED1': 'value',
    'LED2': 'value',
    'LED3': 'value',
    'LED4': 'value',
    'tempVal': 'value',
    'lumiVal': 'value',
    'tempTh': 'value',
    'lumiTh': 'value',
    'LED_RGB' : {
        'red': 'value',
        'green': 'value',
        'blue': 'value',
    },
    'NIGHT': 'value'
};

//Déclaration de l'adresse du server OM2M et du port
var ip_server_om2m = "192.168.0.189";
var port_server_om2m = "8080";


//Détection de la page sur laquelle est le client pour mettre à jour la variable profile


app.get('/:page', function(req, res) {
    res.sendFile(__dirname+'/'+req.params.page+'.html');
    console.log("on est sur la page de " + req.params.page);
    
    if(req.params.page !== 'index') //S'il ne s'agit pas de la page d'index et donc d'une page de réglages de profil
        {
            //profile prend la valeur de l'ID de USER qui correspond au nom de la page
            profile = tableProfiles[req.params.page];
            console.log("profil de " + profile);
            
            //On envoie une requête pour demander les paramètres actuels
             var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse/mn-name/"+profile+"?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            //Envoi de la requête

            var req = http.request(options_request, function (res) {
          var chunks = [];

          res.on("data", function (chunk) {
            chunks.push(chunk);
          });

          res.on("end", function () {
            var body = Buffer.concat(chunks);
            console.log(body.toString());
            parseString(body.toString(), function (err, result) {
                        param_user.home = result.obj.bool[0].$.val;
                        param_user.tempTh = parseInt(result.obj.int[0].$.val,10);
                        param_user.lumiTh = parseInt(result.obj.int[1].$.val,10);
                        param_user.LED2 = result.obj.bool[1].$.val;
                        param_user.LED1 = result.obj.bool[2].$.val;
                        param_user.LED_RGB.red = parseInt(result.obj.int[2].$.val,10);
                        param_user.LED_RGB.green = parseInt(result.obj.int[3].$.val,10);
                        param_user.LED_RGB.blue = parseInt(result.obj.int[4].$.val,10);
                        
                        console.log(param_user);
                        io.sockets.emit('user_update',param_user);              
            });
          });
        });

        req.end();
    
        }else{ //----------------------------------------------------------------------------------
            
              /*
     * Toutes les 'interval' millisecondes (10 secondes), on envoie des requêtes pour connaitre 
     * la valeur des capteurs et actionneurs
     */

            console.log("Récupération périodique des valeurs de capteurs :");

            //Création des options de la requête en y intégrant la variable profile pour différencier les users
            var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse/mn-name/LED1?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.LED1 = result.obj.bool[0].$.val;
                        param_manual.lumiTh = result.obj.real[0].$.val;
                        console.log(param_manual);
                    });
                });
            });
            req.end();
        
        //-------------------------------------------------------------------
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse/mn-name/LED2?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.LED2 = result.obj.bool[0].$.val;
                        console.log(param_manual);
                    });
                });
            });
            req.end();
        
        //-------------------------------- LED3 -----------------------------------
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse2/mn-name/LED3?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.LED3 = result.obj.bool[0].$.val;
                        console.log(param_manual);
                    });
                });
            });
            req.end();
        
        //-------------------------------- LED4 -----------------------------------
            
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse2/mn-name/LED4?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.LED4 = result.obj.bool[0].$.val;
                        console.log(param_manual);
                    });
                });
            });
            req.end();
            
        //-------------------------------- NIGHT -----------------------------------
            
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse2/mn-name/NIGHT?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.NIGHT = result.obj.bool[0].$.val;
                        console.log(param_manual);
                    });
                });
            });
            req.end();
        
        //-------------------------------------------------------------------
            
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse/mn-name/TEMPERATURE?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.tempVal = parseInt(result.obj.real[0].$.val,10);
                        console.log(param_manual);
                    });
                });
            });
            req.end();
        
        //-------------------------------------------------------------------
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse/mn-name/LUMINOSITE?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.lumiVal = parseFloat(result.obj.int[0].$.val,10);
                        console.log(param_manual);
                    });
                });
            });
            req.end();
            
             //-------------------------------------------------------------------
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse/mn-name/FAN?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.tempTh = parseFloat(result.obj.real[0].$.val,10);
                        console.log(param_manual);
                    });
                });
            });
            req.end();
            
        }
});
    
    
    //------------------------------------------------------------------------------------
    //FONCTIONNE CORRECTEMENT
    io.sockets.on('connection',function(socket) {
        
        socket.emit('user_update',param_user);    
        socket.emit('device_update',param_manual);
        
    console.log("new coo");              
    
    socket.on('device_set', function(data) {
        console.log(data);
        
        ////// POSTMAN //////
        var options = {
          "method": "POST",
          "hostname": ip_server_om2m,
          "port": port_server_om2m,
          "path": "/~/"+data.gateway+"/mn-name/"+data.device+"?op="+data.value,
          "headers": {
            "x-m2m-origin": "admin:admin",
            "cache-control": "no-cache",
            "postman-token": "e4d9c61f-2105-d230-5a31-f1791de2c499"
          }
        };
        console.log("Envoi : "+options.path);


        var req = http.request(options, function (res) {
          var chunks = [];

          res.on("data", function (chunk) {
            chunks.push(chunk);
          });

          res.on("end", function () {
            var body = Buffer.concat(chunks);
            console.log(body.toString());
          });
            
        });

        req.end();

    });
    
    //------------------------------------------------------------------------------------
    //FONCTIONNE CORRECTEMENT
    socket.on('user_set', function(data) {
        console.log(data);
        
        ////// POSTMAN //////
        var options = {
          "method": "POST",
          "hostname": ip_server_om2m,
          "port": port_server_om2m,
          "path": "/~/"+data.gateway+"/mn-name/"+data.user+"?op="+data.device+"/"+data.value,
          "headers": {
            "x-m2m-origin": "admin:admin",
            "cache-control": "no-cache",
            "postman-token": "e4d9c61f-2105-d230-5a31-f1791de2c499"
          }
        };
        console.log("Envoi : "+options.path);


        var req = http.request(options, function (res) {
          var chunks = [];

          res.on("data", function (chunk) {
            chunks.push(chunk);
          });

          res.on("end", function () {
            var body = Buffer.concat(chunks);
            console.log(body.toString());
          });
        });

        req.end();
        
        //Réponse avec un get
        var options_request = {
          "method": "POST",
          "hostname": ip_server_om2m,
          "port": port_server_om2m,
          "path": "/~/"+data.gateway+"/mn-name/"+data.user+"?op=get",
          "headers": {
            "x-m2m-origin": "admin:admin",
            "cache-control": "no-cache",
            "postman-token": "e4d9c61f-2105-d230-5a31-f1791de2c499"
          }
        };

            //Envoi de la requête

            var req = http.request(options_request, function (res) {
          var chunks = [];

          res.on("data", function (chunk) {
            chunks.push(chunk);
          });

          res.on("end", function () {
            var body = Buffer.concat(chunks);
            console.log(body.toString());
            parseString(body.toString(), function (err, result) {
                        param_user.home = result.obj.bool[0].$.val;
                        param_user.tempTh = parseInt(result.obj.int[0].$.val,10);
                        param_user.lumiTh = parseInt(result.obj.int[1].$.val,10);
                        param_user.LED2 = result.obj.bool[1].$.val;
                        param_user.LED1 = result.obj.bool[2].$.val;
                        param_user.LED_RGB.red = parseInt(result.obj.int[2].$.val,10);
                        param_user.LED_RGB.green = parseInt(result.obj.int[3].$.val,10);
                        param_user.LED_RGB.blue = parseInt(result.obj.int[4].$.val,10);
                        
                        console.log(param_user);  
                
            });
            
            socket.emit('user_update',param_user);
          });
        });

        req.end();

    });
        
        
        
        
        
        
        
        
        
        //////// FONCTION SET INTERVAL ////////
        
        
        
        setInterval(function() {
            /*
     * Toutes les 'interval' millisecondes (10 secondes), on envoie des requêtes pour connaitre 
     * la valeur des capteurs et actionneurs
     */

            console.log("Récupération périodique des valeurs de capteurs :");

            //Création des options de la requête en y intégrant la variable profile pour différencier les users
            var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse/mn-name/LED1?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.LED1 = result.obj.bool[0].$.val;
                        param_manual.lumiTh = result.obj.real[0].$.val;
                        console.log(param_manual);
                    });
                });
            });
            req.end();
        
        //-------------------------------------------------------------------
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse/mn-name/LED2?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.LED2 = result.obj.bool[0].$.val;
                        console.log(param_manual);
                    });
                });
            });
            req.end();
        
        //-------------------------------------------------------------------
      /*  var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse1/mn-name/LED3?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.LED3 = result.obj.bool[0].$.val;
                        console.log(param_manual);
                    });
                });
            });
            req.end();*/
        
        //-------------------------------------------------------------------
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse/mn-name/TEMPERATURE?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.tempVal = parseInt(result.obj.real[0].$.val,10);
                        console.log(param_manual);
                    });
                });
            });
            req.end();
        
        //-------------------------------------------------------------------
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse/mn-name/LUMINOSITE?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.lumiVal = parseFloat(result.obj.int[0].$.val,10);
                        console.log(param_manual);
                    });
                });
            });
            req.end();
            
             //-------------------------------------------------------------------
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse/mn-name/FAN?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.tempTh = parseFloat(result.obj.real[0].$.val,10);
                        console.log(param_manual);
                    });
                });
            });
            req.end();
            
            //-------------------------------- LED3 -----------------------------------
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse2/mn-name/LED3?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.LED3 = result.obj.bool[0].$.val;
                        console.log(param_manual);
                    });
                });
            });
            req.end();
        
        //-------------------------------- LED4 -----------------------------------
            
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse2/mn-name/LED4?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.LED4 = result.obj.bool[0].$.val;
                        console.log(param_manual);
                    });
                });
            });
            req.end();
            
        //-------------------------------- NIGHT -----------------------------------
            
        var options_request = {
                "method": "POST",
                "hostname": ip_server_om2m,
                "port": port_server_om2m,
                "path": "/~/mn-cse2/mn-name/NIGHT?op=get",
                "headers": {
                "x-m2m-origin": "admin:admin",
                "cache-control": "no-cache",
                "postman-token": "1679f03c-71ba-1d6f-4896-d32c99062ff3"
                }
            };

            var req = http.request(options_request, function (res) {
                var chunks = [];
                
                res.on("data", function (chunk) {
                    chunks.push(chunk);
                });
                
                res.on("end", function () {
                    var body = Buffer.concat(chunks);
                    console.log(body.toString());
                    
                    parseString(body, function (err, result) {
                        param_manual.NIGHT = result.obj.bool[0].$.val;
                        console.log(param_manual);
                    });
                });
            });
            req.end();
        
            socket.emit('device_update',param_manual);
        
        }, interval);
    
});


console.log("Listening on port 5050");
server.listen(5050);