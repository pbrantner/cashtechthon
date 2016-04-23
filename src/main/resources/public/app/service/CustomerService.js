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
            get : function(customerId) {
                /*
                var data = {"name": "User1", "userId": customerId, "key2":"value2", "key3":"value3","key4":"..."};
                data.avatar = "svg-" + customerId;
                //var data = $http.get('/classifications/' + customerId);
                return $q.when(data);
                */
                //return $http.get('/classifications?...');
                return $http.get('/customers/' + customerId);
            }
        };
    }
})();
