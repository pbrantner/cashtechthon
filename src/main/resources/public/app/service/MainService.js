(function(){
    'use strict';

    angular.module('MDC')
        .service('mainService', ['$q', MainService]);

    /**
     * Main Data Service
     *
     * @returns {{loadAll: Function}}
     */
    function MainService($q) {
        return {
            loadAllCustomers : function() {
                var customers = [{"name": "User1", customerId: 1, avatar: "svg-1"}, {"name":"User2", customerId: 2, avatar: "svg-2"}];
                //var customers = $http.get('/customers');


                return $q.when(customers);
            }
        };
    }

})();
