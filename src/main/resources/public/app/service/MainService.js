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
                var customers = [{"name": "User1"}, {"name":"User2"}];
                //var customers = $http.get('/customers');


                return $q.when(customers);
            }
        };
    }

})();
