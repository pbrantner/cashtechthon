# API Draft V0.1

### ```POST /files```

Uploads a file as ```multipart/form-data```

#### Response

+ **201** File has been uploaded; returns response
+ **409** No disk space, etc.

```javascript
{"fileid":14}
```

### ```POST /transactions```

Imports transactions into the database from a file.

#### Request

```javascript
{"fileid":14}
```

#### Response

+ **202** Import has been started
+ **404** File with ```:fileid``` does not exist
+ **409** File with ```:fileid``` has been already imported/is being imported

### ```GET /customers?offset=:offset&limit=:limit```

Returns ```:limit``` users offset by ```:offset``` ordered by most recenty imported transactions.

#### Response

```javascript
[{"firstname":"John"
 ,"lastname":"Doe"
 ,"customerid":1
 }
,...
,{"firstname":"Jane"
 ,"lastname":"Doe"
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

#### GET Parameters

+ include[] (optional)
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