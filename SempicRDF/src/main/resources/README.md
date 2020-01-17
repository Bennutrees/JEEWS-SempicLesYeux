#Web sémantique


-Préfixes utilisées
```sql
PREFIX ex: <http://example.org/>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>
prefix owl: <http://www.w3.org/2002/07/owl#>
prefix projet: <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#>
prefix xsd:<http://www.w3.org/2001/XMLSchema#>
```

- Récupère toutes les classes utilisés
```sql
SELECT distinct ?x WHERE {
	?x rdf:type owl:Class
}
```

- Récupère toutes les images ayant comme titre "Cadeau Eric"
```sql
SELECT distinct ?picture
  WHERE {
    ?picture a projet:Picture;
             projet:Title "Cadeau Eric".
  }
```
![enter image description here](https://lh3.googleusercontent.com/b5hgPDk5DUHq5AJ7xUefNjIss2SNaPPwa9nRadFgyw7jkIEVH_SXDW_-1q6DrP957PqtQGDLCOE)
- Récupère toutes les images et leur titre
```sql
SELECT distinct ?picture ?title
  WHERE {
    ?picture a projet:Picture;
             projet:Title ?title.
  }
```

- Récupère toutes les images et leurs auteurs
```sql
SELECT distinct ?picture ?author
  WHERE {
    ?picture a projet:Picture;
             projet:Author ?author.
  }
```
- Récupère toutes les images en fonction d'un auteur
```sql
SELECT distinct ?picture
  WHERE {
    ?picture a projet:Picture;
             projet:Author projet:Edouard_DELATTRE.
  }
```

- Récupère toutes les images et leurs sujet
```sql
SELECT distinct ?picture ?sujet
  WHERE {
    ?picture a projet:Picture;
             projet:Subject ?sujet.
  }
```

- Récupère toutes les images et leurs sujet et leur sujet si ces derniers sont des monuments
```sql
 SELECT distinct ?picture ?sujet
  WHERE {
    ?picture a projet:Picture;
             projet:Subject ?sujet.
  
  	?sujet a projet:Monuments.
  }
```

- Récupère toutes les images et les personnes présentes dans cette photo
```sql
SELECT distinct ?picture ?figurants
  WHERE {
    ?picture a projet:Picture;
             projet:Who ?figurants.
  }
```

- Récupère toutes les images en fonction d'une personne présent dans la photo
```sql
SELECT distinct ?picture
  WHERE {
    ?picture a projet:Picture;
             projet:Who projet:Adem_GURBUZ.
  }
```

- Récupère toutes les images et leur date
```sql
SELECT distinct ?picture ?date
  WHERE {
    ?picture a projet:Picture;
             projet:Date ?date.
  }
```

- Récupère toutes les images en fonction d'une date
```sql
SELECT distinct ?picture ?date
  WHERE {
    ?picture a projet:Picture;
             projet:Date ?date.
  filter (?date = "2019-01-01T00:00:00").
  }
```

- Récupère toutes les images entre deux dates
```sql
SELECT distinct ?picture ?date
  WHERE {
    ?picture a projet:Picture;
             projet:Date ?date.
  filter (?date > "2018-01-01T00:00:00").
  filter (?date < "2020-01-01T00:00:00").
  }
```
![enter image description here](https://lh3.googleusercontent.com/mT4EFnaE9Nqsh31IZl0hqeaJkW95ShaoWwwMRgXPIGJ52as3sWGU7HrJmko20D9h9uriaskZ2VY)

-- Récupère tout les selfies
```sql
SELECT DISTINCT ?picture ?subject
  WHERE {
    ?picture a projet:Picture;
             projet:Author ?subject;
             projet:Subject ?subject
  }
```

-- Récupère tout les selfies d'une personne demandé 
```sql
SELECT DISTINCT ?picture 
  WHERE {
    ?picture a projet:Picture;
             projet:Author projet:Adem_GURBUZ;
             projet:Subject projet:Adem_GURBUZ.
  }
```

-- Récupère toutes les photos avec une seul personne présente
```sql
SELECT DISTINCT ?picture (count(?who) as ?count)
  WHERE {
    ?picture a projet:Picture;
             projet:Who ?who.
  }
group BY ?picture
having(?count = 1)
```

  -- Récupère toutes les photos avec une seul personne choisit présente
```sql
SELECT DISTINCT ?picture (count(?who) as ?count)
  WHERE {
    ?picture a projet:Picture;
             projet:Who projet:Adem_GURBUZ;
  			 projet:Who ?who
  }
group BY ?picture
having(?count = 1)
```

-- Récupère toutes les photos de groupes (plusieurs personnes présentes).
```sql
SELECT DISTINCT ?picture (count(?who) as ?count)
  WHERE {
    ?picture a projet:Picture;
             projet:Who ?who.
  }
group BY ?picture
having(?count != 1)
  ```

-- Récupère toutes les photos ou des animaux apparaissent
```sql
SELECT DISTINCT ?picture
  WHERE {
    ?picture a projet:Picture;
             projet:Subject ?animal.
  	
    ?animal a ?Type.
    	?Type rdfs:subClassOf projet:Animal
  }
```

-- Récupère toutes les photos ou un type d'animaux apparaît (Changer le Dog pour modifier le résultat)
```sql
SELECT DISTINCT ?picture
  WHERE {
    ?picture a projet:Picture;
             projet:Subject ?typeAnimal.
  
  	?typeAnimal a projet:Dog.
  }
```

/////////////////////////////////////////////////////////////////////////////////////////////Erreur
-- Récupère toutes les photos d'un animal de compagnie de quelqu'un
```sql
SELECT DISTINCT ?picture
  WHERE {
  
   ?person a ?personType.
  		?personType rdfs:subClassOf projet:Person.
   
   ?animal a ?Type.
    	?Type rdfs:subClassOf projet:Animal.

   ?person projet:Pet ?animal.
           
    projet:Eric_DELATTRE projet:Pet ?animal.
  
    ?picture a projet:Picture;
             projet:Subject ?animal.  	
  }
```

-- Récupère toutes les photos entres amis
```sql
  SELECT DISTINCT ?picture
  WHERE {
    ?picture a projet:Picture;
        projet:Who ?person;
        projet:Who ?personFriend.
  
    ?person a ?personType.
    ?personFriend a ?personType.
    ?personType rdfs:subClassOf projet:Person.
  
    ?person projet:Friend ?personFriend.
    ?personFriend projet:Friend ?person.
}
```

-- Récupère toutes les photos entre collèges (On affiche aussi les noms des collèges
```sql
 SELECT distinct ?picture ?person ?colleague
  WHERE {
     ?picture a projet:Picture;
              projet:Who ?person;
  			  projet:Who ?colleague.
  
  	?person projet:Colleague ?personColleague.
  	?person projet:Works ?personJob.
  
  	?personJob projet:Colleague ?colleague.
    
  }
```
![enter image description here](https://lh3.googleusercontent.com/JymtPO0mEymX6E_5Si7_A8ldM66jXLCPpW8bnkr4mkDKNX8VjlF5QeB1D4qgXvBUOgtZbkZ1bvg)

////////////////////////////////////////////////////////////////Pas finis je crois
-- Récupère toutes les photos entres amis et collèges
```sql
 SELECT DISTINCT ?picture
  WHERE {
    ?picture a projet:Picture;
        projet:Who ?person;
        projet:Who ?personFriend.
  
    ?person a ?personType.
    ?personFriend a ?personType.
    ?personType rdfs:subClassOf projet:Person.
  	?colleague a ?colleagueType.
  	?colleagueType rdfs:SubClassOf projet:Job.
  
    ?person projet:Friend ?personFriend.
    ?person projet:Colleague ?colleague.
    ?personFriend projet:Friend ?person.
}
```


-- Récupère tout les photos prise par un ou une artiste
```sql
 SELECT DISTINCT ?picture ?author ?authorJob
  WHERE {
    ?picture a projet:Picture;
        projet:Author ?author.
  
    ?author projet:Works ?authorJob.
    ?authorJob a projet:Photographer
}
```

-- Récupère tout les photos prise par UNE artiste entre janvier 2018 et janvier 2021
```sql
 SELECT DISTINCT ?picture ?author ?authorJob ?date
  WHERE {
    ?picture a projet:Picture;
        projet:Date ?date;
        projet:Author ?author.
   
    ?author a projet:Female.
    ?author projet:Works ?authorJob.
    ?authorJob a projet:Photographer.
  filter (?date > "2018-01-01T00:00:00").
  filter (?date < "2021-01-01T00:00:00").
}
```
![enter image description here](https://lh3.googleusercontent.com/ihae6iTMkL1EGdjqu6l66QivGHyWtbh4rGY6nny7W6R1-t3JwbV53ofTbPjPBsGHxRFZV-hyF9M)


-- Récupère tout les photos présentant plusieurs des frères et soeurs
```sql
  SELECT DISTINCT ?picture ?person1 ?person2
  WHERE {
    ?picture a projet:Picture;
        projet:Who ?person1;
        projet:Who ?person2.
  
  		?person1 projet:hasSister ?person2.
        ?person2 projet:hasBrother ?person1.
}
```

-- Récupère tout les photos prises par un appareil photo
```sql
 SELECT DISTINCT ?picture ?Appareil
  WHERE {
    ?picture a projet:Picture;
        projet:Taken_By ?Appareil.
  
  	?Appareil a projet:Camera
}
```

-- Récupère tout les photos prises par un téléphone de type Samsung lors d'un événement musical dans une région affichant une seul personnes connu
```sql
SELECT DISTINCT ?picture (count(?who) as ?count)
  WHERE {
    ?picture a projet:Picture;
            projet:Taken_By ?Appareil;
            projet:Who ?who;
  			projet:Event ?Event;
    	    projet:Where ?Lieux.
  
        ?Appareil a projet:Samsung.
        ?Event a projet:Musical.
        ?Lieux a projet:Region.
  }
group BY ?picture
having(?count != 1)
```

-- Récupère tout les photos prises par une caméra lors d'un événement musical sur la plage affichant plusieurs personnes connu
```sql
SELECT DISTINCT ?picture (count(?who) as ?count)
  WHERE {
    ?picture a projet:Picture;
            projet:Taken_By ?Appareil;
            projet:Who ?who;
  			projet:Event ?Event;
    	    projet:Where ?Lieux.
  
        ?Appareil a projet:Samsung.
        ?Event a projet:Musical.
        ?Lieux a projet:Region.
  }
group BY ?picture
having(?count != 1)
```
![enter image description here](https://lh3.googleusercontent.com/jFxJwnt2CIDJXztU6GgcEvBRts9ztlvl0vzBI-w4E2eyK07D_5lrWq0a8yup2ZghTbuk0X4YuRM)

-- Récupère tout les photos prises pris dans une ville
```sql
SELECT DISTINCT ?picture ?Lieux
  WHERE {
    ?picture a projet:Picture;
    	     projet:Where ?Lieux.
    ?Lieux a projet:City.
  }
```
![enter image description here](https://lh3.googleusercontent.com/5Sib-TaBzjqJxs-doxXuksD7orYOn2r5NA2e1JOrBNnlmReRbtKt2rZIi3y-BMzIkcYbGBcxpdYU)

-- Récupère tout les photos prises pris dans une ville en France
```sql
SELECT DISTINCT ?picture ?Lieux
  WHERE {
    ?picture a projet:Picture;
    	     projet:Where ?Lieux.
    ?Lieux a projet:City.
    ?Lieux projet:Is_in_the_region ?region.
  
  	?region a projet:Region.
  	?region projet:Is_in_the_Country projet:France
  
  }
```
![enter image description here](https://lh3.googleusercontent.com/tADRyLLX2fnScCuCIs4NIGmP1y0BGg0MWkl96sJUo4c9BzuZtRgDtX3NQiJ3DVc-6lBZvmpQK9Of)

-- Récupère tout les selfies et optionnellement les titres si ces derniers existent
```sql
SELECT DISTINCT ?picture ?subject ?title
  WHERE {
    ?picture a projet:Picture;
             projet:Author ?subject;
             projet:Subject ?subject.
  	OPTIONAL { ?picture projet:Title ?title }
  }
```

-- Récupère toutes les photo ou Etienne DELATTRE apparaît ou alors les photos de paysages
```sql
SELECT DISTINCT ?picture ?subject ?personnes ?author
  WHERE {
    ?picture a projet:Picture;
             projet:Author ?author;
             projet:Subject ?subject.
  	OPTIONAL { ?picture projet:Who ?personnes}
  	FILTER ( !bound(?personnes) || ?personnes = projet:Etienne_DELATTRE )
  }
```

-- Récupère toutes les photo ou le sujet est un animal ou si une personne est le sujet
```sql
SELECT DISTINCT ?picture ?animal
  WHERE {
    ?picture a projet:Picture;
             projet:Subject ?animal.
  	
    {?animal a ?personType.
    ?personType rdfs:subClassOf projet:Person}
  union 
    {?animal a ?Type.
    ?Type rdfs:subClassOf projet:Animal}.
  }
```

SERVICE <http://dbpedia.org/sparql> {

SELECT DISTINCT ?picture ?Lieux
  WHERE {
    ?picture a projet:Picture;
    	     projet:Where ?Lieux.
    ?Lieux a projet:City.
    ?Lieux projet:Is_in_the_region ?region.
  
  	?region a projet:Region.
  	?region projet:Is_in_the_Country projet:France
  }




SELECT DISTINCT ?picture ?Lieux ?test ?dbCountryName ?dbCountry ?arrondissement
  WHERE {
    ?picture a projet:Picture;
    	     projet:Where ?Lieux.
    ?Lieux a projet:City.
    ?Lieux projet:Is_in_the_region ?region.
  	?Lieux projet:CityName ?test.
  
  	?region a projet:Region.
  	?region projet:Is_in_the_Country projet:France.
  
  SERVICE <http://dbpedia.org/sparql> {
    ?dbCountry a <http://schema.org/Place>.
     ?dbCountry owl:sameAs ?dbCountryName.
     ?dbCountryName foaf:name ?arrondissement
     FILTER(?dbCountryName = ?countryName).
    
  }
  }