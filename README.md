# NewsApp

Application pour récupérer les dernières informations en France.  
Dans cette application, le choix du sujet n'est pas donné à l'utilisateur et par simplicité il sera donc "kotlin" par défaut.

## Dependency injection

J'utilise `Hilt` pour gérer l'injection de dépendance.  
Notamment celle du module concernant l'API, en injectant `Retrofit`.

## MVVM

L'application utilise une architecture MVVM, néanmoins il n'y a pas de stockage local.  
J'aurais pu rajouter un système de cache lors de la réception des informations via l'API, mais l'utilité était minimale dans cet exemple.

Le viewmodel utilise des `LiveData` pour la vue, et le `remote repository` (API) utilise les kotlin `Flow` afin de prendre avantage des `coroutines`.

## Network

L'identification au site NewsApi requiert une clé API, qui est fournie par un intercepteur HTTP de `Retrofit`, qui va injecter la clé dans les headers des requêtes.  
La clé est dans un fichier de config (`apikey.properties`), qui est ignoré du `Git` pour des raisons évidentes de sécurité.  
Afin de tester l'application il faudra un fichier à la racine du projet.

`apikey.properties` :

```properties
NEWS_API_KEY="XXXXXXXXXXXXXXXXXXXX"
```

Les données reçues sont automatiquement désérialisées via `GSON`. Les données visibles sont directement celles fournies par l'API. S' il existe des bugs d'encodages, de textes vides ou autres, cela vient uniquement de ce qui est produit par l'API. Le but n'étant pas de débugger chaque petit détail de l'API, je ne suis pas allé plus loin.

## UI

J'utilise un template par défaut Android Studio de liste + détail, auquel j'ai uniquement modifié les données et certaines UI pour accueillir mes données.  
Pour l'affichage des articles j'ai ajouté la librairie `Cardview`.  
Enfin j'utilise `Glide` pour l'affichage des images.

## Test

Un exemple de test sur un appel API réussi est effectué. Il permet de vérifier que le comportement de l'application est bien celui attendu.

On pourrait réaliser des tests sur beaucoup d'autres fonctionnalités de l'application, mais c'est bien trop chronophage et omis par simplicité.  
On aurait pu tester les endpoints, le mapping des données reçues, l'UI, etc...
