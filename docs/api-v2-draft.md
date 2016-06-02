# API Draft V0.2

### ```GET /locations```

Returns a list of all saved locations.

#### Response

```javascript
{"locations":
 ["Vienna"
 ,"St. Pölten"
 ,"Graz"
 ]
}
```

### ```GET /classifications```

#### Response 

```javascript
{"classifications":
 ["Groceries"
 ,"Rent"
 ,"Income"
 ]
}
```

### ```GET /customers/:customerid```

Returns customer with ```:customerid```.

#### Response

+ **200** Returns response
+ **404** Customer with ```:customerid``` does not exist

```javascript
{"firstname":"John"
,"lastname":"Doe"
,"age":21
,"sex":0
,"location":"Vienna"
,"customerid":1
 }
```

### ```GET /customers```

Returns customers ordered by most recent classification.

#### GET Parameters

+ page (optional)
	- **Default**: 1
+ limit (optional)
	- **Default**: 10

#### Response

+ **200** Returns response

```javascript
[{"firstname":"John"
 ,"lastname":"Doe"
 ,"age":21
 ,"sex":0
 ,"location":"Vienna"
 ,"customerid":1
 }
,...
,{"firstname":"Jane"
 ,"lastname":"Doe"
 ,"age":23
 ,"sex":1
 ,"location":"Vienna"
 ,"customerid":100
 }
]
```

### ```GET /customers/:customerid/classifications```

Returns classifications of customer with ```:customerid``` ordered by transactionDateTime (newest first).

#### GET Parameters

+ page (optional)
	- **Default**: 1
+ limit (optional)
	- **Default**: 10

#### Response

+ **200** Returns response
+ **404** Customer with ```:customerid``` does not exist

```javascript
[{"currency":"EUR"
 ,"amount":-20
 ,"classification":"Transportation"
 ,"transactionDateTime":"2016-04-23T18:25:43.511Z"
 },
 ...
 {"currency":"EUR"
 ,"amount":-420
 ,"classification":"Rent"
 ,"transactionDateTime":"2016-04-23T18:25:43.511Z"
 }
]
```

### ```GET /customers/:customerid/classifications/summary```

Returns the percentage of each classification of customer with ```:customerid```.

> Does not need to be implemented

#### Response

+ **200** Returns response
+ **404** Customer with ```:customerid``` does not exist

```javascript
{"Expenses":
 ["Groceries":0.33
 ,"Transportation":0.2
 ,"Health":0.05
 ,"Rent":0.42
 ],
 "Earnings":
 ["Income":0.82
 ,"Subsidy":0.18
 ]
}
```

### ```GET /customers/:customerid/classifications/comparison```

Returns the classification summary for customer with ```:customerid``` and the group matching the given ```GET Parameters```.

#### Response

#### GET Parameters

+ ageFrom (optional)
	- **Values**: *18, 20, 25, ...*
	- **Description**: Limits the comparision to customers who are atleast age-from old.
+ ageTill (optional)
	- **Values**: *18, 20, 25, ...*
	- **Description**: Limits the comparision to customers who are atmost age-till old.
+ sex (optional)
	- **Values**: *0 or 1*
	- **Description**: Limits the comparision to customers who are of sex.
+ incomeFrom (optional)
	- **Values**: *15000, 20100, 70200, ...*
	- **Description**: Limits the comparision to customers who have a min. monthly income of income-from. 
+ incomeTill (optional)
	- **Values**: *15000, 20100, 70200, ...*
	- **Description**: Limits the comparision to customers who have a max. monthly income of income-till.
+ location (optional)
	- **Values**: *Vienna, Graz, ...*
	- **Description**: Limits the comparison to customers within the given location
+ classification (optional)
	- **Values**: *Groceries, Income, Transportation, ...*
	- **Description**: Limits the comparison to the given classification. If this parameter is given all returned numbers must be absolute.

#### Response

+ **200** Returns response
+ **404** Customer with ```:customerid``` does not exist

```javascript
{"customer":
 {"Expenses":
  ["Groceries":0.33
  ,"Transportation":0.2
  ,"Health":0.05
  ,"Rent":0.42
  ],
  "Earnings":
  ["Income":0.82
  ,"Subsidy":0.18
  ]
 },
 "group":
 {"Expenses":
  ["Groceries":0.32
  ,"Transportation":0.21
  ,"Health":0.04
  ,"Rent":0.46
  ],
  "Earnings":
  ["Income":0.8
  ,"Subsidy":0.2
  ]
 }
}
```

### ```GET /customers/:customerid/comparison```

Returns comparison between customer with ```:customerid``` and group matching given ```GET Parameters``` over months.

#### GET Parameters

+ pastMonths
	- **Values**: *1, 2, 8, ...*
	- **Description**: Limits the comparision from (today - past-months) till today.
+ ageFrom (optional)
	- **Values**: *18, 20, 25, ...*
	- **Description**: Limits the comparision to customers who are atleast age-from old.
+ ageTill (optional)
	- **Values**: *18, 20, 25, ...*
	- **Description**: Limits the comparision to customers who are atmost age-till old.
+ sex (optional)
	- **Values**: *0 or 1*
	- **Description**: Limits the comparision to customers who are of sex.
+ incomeFrom (optional)
	- **Values**: *15000, 20100, 70200, ...*
	- **Description**: Limits the comparision to customers who have a min. monthly income of income-from. 
+ incomeTill (optional)
	- **Values**: *15000, 20100, 70200, ...*
	- **Description**: Limits the comparision to customers who have a max. monthly income of income-till.
+ location (optional)
	- **Values**: *Vienna, Graz, ...*
	- **Description**: Limits the comparison to customers within the given location
+ classification (optional)
	- **Values**: *Groceries, Income, Transportation, ...*
	- **Description**: Limits the comparison to the given classification. If this parameter is given all returned numbers must be absolute.

#### Response

+ **200** Returns response
+ **404** Customer with ```:customerid``` does not exist

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