(function(){
    'use strict';

    angular.module('MDC')
        .service('customerService', ['$http', CustomerService]);

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
            getCompanies : function(customerId){
                return $http.get("/customers/:id/companies", {id:customerId});
            }
        };
    }
})();
