(function(){
    'use strict';

    angular.module('MDC')
        .service('customerService', ['$q', '$http', CustomerService]);

    /**
     * customer-Details Data Service
     *
     * @returns {{get: Function}}
     */
    function CustomerService($q, $http) {
        return {
            get : function(customerId) {
                return $http.get('/classifications/' + customerId);
            }
        };
    }
})();
