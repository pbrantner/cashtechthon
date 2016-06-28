(function(){
    'use strict';

    angular.module('MDC')
        .service('mainService', ['$http', MainService]);

    /**
     * Main Data Service
     *
     * @returns {{loadAll: Function}}
     */
    function MainService($http) {
        return {
            loadAllCustomers : function() {
                return $http.get('/customers?size=1000');
            }
        };
    }

})();
