(function(){
    'use strict';

    angular.module('MDC')
        .service('mainService', ['$q', '$http', MainService]);

    /**
     * Main Data Service
     *
     * @returns {{loadAll: Function}}
     */
    function MainService($q, $http) {
        return {
            loadAllCustomers : function() {
                /*
                var customers = [{
                    "name": "User1",
                    customerId: 1,
                    accountNumber: 123456,
                    avatar: "svg-1"
                }, {
                    "name":"User2",
                    customerId: 2,
                    accountNumber: 5464,
                    avatar: "svg-2"
                }];
                //var customers = $http.get('/customers');


                return $q.when(customers);
                */
                return $http.get('/customers');
            }
        };
    }

})();
