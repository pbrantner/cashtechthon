(function(){
    'use strict';

    angular.module('MDC')
        .service('CustomerService', ['$http', CustomerService]);

    /**
     * customer-Details Data Service
     *
     * @returns {{get: Function}}
     */
    function CustomerService($http) {
        return {
            get : function(customerId, from, till) {
                return $http.get('/classifications?customers=' + customerId
                    + '&from=' + from + '&till=' + till);
            },
            getCsv: function(customerId, from, till) {
                return $http.get('/classifications?customers=' + customerId
                    + '&from=' + from + '&till=' + till + '&format=csv');
            },
            getCustomer : function(customerId){
                return $http.get("/customers/"+ customerId);
            },
            getCompanies : function(customerId){
                return $http.get("/customers/"+customerId+"/companies");
            }
        };
    }
})();
