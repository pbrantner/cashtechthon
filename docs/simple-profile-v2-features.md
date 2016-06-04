# Features for Simple Profiles 2.0 

## Thresholds

Properties

* A threshold can be either positive or negative.
	+ A positive threshold is reached when the threshold is reached or exceeded.
	+ A negative threshold is reached when the threshold is reached or deceeded.
* A threshold can be unidirectional or bidirectional
	+ An unidirectional threshold only considers transaction in one direction, meaning it will either consider incoming transaction or outgoing transactions
	+ A bidirectional threshold will consider incoming and outgoing transactions.
* If a threshold is reached an alert will be created for the customer.

Thresholds can be limited to certain transactions by the following constraints:

* Classification

  	The threshold only considers transactions within the given classification.

* Cycle

  	The threshold only considers transactions since the last cycle start. A cycle restarts after a fixed time period.

There are two types of thresholds:

* Absolute
  An absolute threshold alerts about the customer's total account balance.

  	+ Absolute thresholds are always bidirectional.

  	+ Absolute thresholds cannot be constrained to a classification.

	+ Absolute thresholds cannot be cyclic.

  	Examples:
  	
  	+ A negative absolute € 200 threshold creates an alert if the customer's account balance reaches or falls under € 200.
  	  	
  	  	Intuitive: Alert the customer if they have exactly or less than € 200.

  	+ A positive absolute € 200 threshold creates an alert if the customer's account balance reaches or exceeds € 200.
  		
  		Intuitive: Alert the customer if they have exactly or more than € 200.

* Relative

	  A relative threshold alerts about the customer's earnings and spendings relative to a date. 
  	  If no date is specified the date is the creation date of the threshold.
  
  	Example:
  	
  	+ An unidirectional negative relative € 200 threshold creates an alert if the customer spent € 200 since the creation of the threshold.
    
    	  Intuitive: Alert the customer if they spent € 200 since they created the threshold.
  	
  	+ A bidirectional positive relative € 50000 threshold creates an alert if the customer increased their account balance by € 50000 since the creation of the threshold.
  	  
  	  	Intuitive: Alert the customer if they saved € 50000 since they created the threshold.
  	
  	+ A cyclic unidirectional negative relative € 200 threshold constrained to the classification "Groceries" creates an alert if the customer spent € 200 since the start of the current cycle.
  	  	
  	  	Intuitive: Alert the customer if they spent € 200 within the current timeframe (e.g. a month, a week)

## Technical Notes

### Absolute Threshold

#### Create

1.  Neuer Threshold via REST-Schnittstelle
2.  Speichern in DB (neue ID wird generiert)
3.  Momentanen Kontostand aus DB ausrechnen
4.  Kontostand an CEP 
5.  Neue Query für Threshold erstellen mit ID aus DB

#### Update

1.  

#### Occurence

### Relative Threshold