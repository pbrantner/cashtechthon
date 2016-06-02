# API Draft V0.2

### ```POST /transactions```

> TODO change this to classifications

Imports transactions into the database from a file.

#### Request

```javascript
{"fileid":14}
```

#### Response

+ **202** Import has been started
+ **404** File with ```:fileid``` does not exist
+ **409** File with ```:fileid``` has been already imported/is being imported

### ```GET /locations```

Returns a list of all saved locations.

#### Response

```javascript
{"locations":
 ["Wien"
 ,"St. Pölten"
 ,"Graz"
 ]
}
```

### ```GET /customers?offset=:offset&limit=:limit```

> TODO change from offset and limit to Spring's Pageable

Returns ```:limit``` users offset by ```:offset``` ordered by most recenty imported transactions.

#### Response

```javascript
[{"firstname":"John"
 ,"lastname":"Doe"
 ,"age":21
 ,"sex":0
 ,"location":"Wien"
 ,"customerid":1
 }
,...
,{"firstname":"Jane"
 ,"lastname":"Doe"
 ,"age":23
 ,"sex":1
 ,"location":"Wien"
 ,"customerid":100
 }
]
```

### ```GET /customers/:customerid```

Returns customer with :customerid

#### Response

+ **200** Returns response
+ **404** Customer with :customerid does not exist

```javascript
{"firstname":"John"
,"lastname":"Doe"
,"customerid":13
}
```

### ```GET /classifications```

> TODO return all known classifications

#### GET Parameters

+ age[] (optional)
	- **Values**: *bauen, mode, sparen*
	- **Description**: Only classifications in :include[] will be returned
+ exclude[] (optional)
	- **Values**: *bauen, mode*
	- **Description**: Classifications in :exclude[] will not be returned
+ customers[] (optional)
	- **Values**: *123484, 860958, ...*
	- **Description**: Only classifications for :customers[] will be returned
+ from
	- **Values**: *2016-04-24, 2013-01-08, ...*
	- **Description**: Only transactions after and at :from are included
+ till
	- **Values**: *2016-04-30, 2013-01-20, ...*
	- **Description**: Only transactions before and at :till are included
+ format (optional, default: json)
	- **Values**: *json, csv*
	- **Description**: Sets output of response

#### Request

```https://lan.bank.at/mdc/classifications?include=bauen,sparen,mode&customers=948482&from=2016-04-24&till=2016-04-30```

#### Response (json)

```javascript
[{"customerid":13
 ,"firstname":"John"
 ,"lastname":"Doe"
 ,"classifications":["bauen","sparen"]
 }
]
```

#### Response (csv)

|customerid|firstname|lastname|classifications|
|----------|---------|--------|---------------|
|1         |John     |Doe     |"bauen,sparen" |


### ```GET /classifications/summary```

#### GET Parameters

+ include[] (optional)
	- **Values**: *bauen, mode*
	- **Description**: Only classifications in :include[] will be returned
+ exclude[] (optional)
	- **Values**: *bauen, mode*
	- **Description**: Classifications in :exclude[] will not be returned
+ from
	- **Values**: *2016-04-24, 2013-01-08, ...*
	- **Description**: Only transactions after and at :from are included
+ till
	- **Values**: *2016-04-30, 2013-01-20, ...*
	- **Description**: Only transactions before and at :till are included

#### Request

```https://lan.bank.at/mdc/classifications/summary?include=bauen,sparen,mode&from=2016-04-24&till=2016-04-30```

#### Response

```javascript
{"transactionsTotal":10000
,"classificationsTotal":18200
,"customersTotal":150
,"classifications":
 [{"name":"bauen","transactions":3200,"transactionsPercentage":0.32,"customers":54,"customersPercentage":0.36}
 ,{"name":"mode","transactions":5000,"transactionsPercentage":0.5,"customers":109,"customersPercentage":0.73}
 ,{"name":"sparen","transactions":10000,"transactionsPercentage":1,"customers":67,"customersPercentage":0.45}
 ]
]
```

### ```GET /customers/:customerid/comparison```

Returns comparison between customer with ```:customerid``` and group matching given ```GET Parameters``` over months.

#### GET Parameters

+ past-months
	- **Values**: *1, 2, 8, ...*
	- **Description**: Limits the comparision from (today - past-months) till today.
+ age-from (optional)
	- **Values**: *18, 20, 25, ...*
	- **Description**: Limits the comparision to customers who are atleast age-from old.
+ age-till (optional)
	- **Values**: *18, 20, 25, ...*
	- **Description**: Limits the comparision to customers who are atmost age-till old.
+ sex (optional)
	- **Values**: *0 or 1*
	- **Description**: Limits the comparision to customers who are of sex.
+ income-from (optional)
	- **Values**: *15000, 20100, 70200, ...*
	- **Description**: Limits the comparision to customers who have a min. monthly income of income-from. 
+ income-till (optional)
	- **Values**: *15000, 20100, 70200, ...*
	- **Description**: Limits the comparision to customers who have a max. monthly income of income-till.
+ location (optional)
	- **Values**: *Wien, Graz, ...*
	- **Description**: Limits the comparison to customers within the given location
+ classification (optional)
	- **Values**: *Lebensmittel, Einkommen, Transport, ...*
	- **Description**: Limits the comparison to the given classification. If this parameter is given all returned numbers must be absolute.

#### Response

+ **200** Returns response
+ **404** Customer with :customerid does not exist

```javascript
{"start":"2015-11"
,"end":"2016-02"
,"columns":['Month', 'Customer', 'Group']
,"data":
	[['2015-11', 630.77 , 722.10 ]
	,['2015-12', 1050.23, 1802.12]
	,['2016-01', 303.55 , 280.78 ]
	,['2016-02', 405.20 , 480.10 ]
	]
}
```