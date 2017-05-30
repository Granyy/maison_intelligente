# Vers une maison plus intelligente
Projet tuteuré réalisé par Gianni D'Amico, Joram Fillol-Carlini, Paul Guirbal, Matthieu Bougeard et Léo Granier dans le cadre de notre 4ème année à l'INSA de Toulouse en Automatique Electronique.

## IHM

Le dossier contient une interface web complète codée en HTML et CSS, utilisant notamment le framework Bootstrap.
Il est associé à un serveur Node.js (app.js) chargé de faire le lien entre le serveur OM2M et l'interface web.

## Jasper

Le dossier contient les modules python codés pour interagir avec le serveur OM2M et délivrer des bulletins météo grâce à l'API OpenWeatherMap.

## NodeRed

Le dossier contient la trace Json des deux flows de données utilisés dans NodeRed pour consulter le nombre d'emails non lus et envoyer des emails de notification.

## Plug-ins OM2M

Le dossier contient les fichiers sources du code java des plugs-in OM2M : home et bedroom. home regroupe différents packages concernant les aspects utilisateurs (rfid), notification (notifier), environnement avec contrôle de la lumière, luminosité et température (environment) et sécuritaire (alarme). bedroom concerne la gestion de la deuxième gateway avec le contrôle de la lumière et du mode nuit.

