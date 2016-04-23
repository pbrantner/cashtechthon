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
                return $http.get('/customers');
            }
        };
    }

})();
