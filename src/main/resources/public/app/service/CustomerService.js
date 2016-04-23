(function(){
    'use strict';

    angular.module('MDC')
        .service('customerService', ['$q', CustomerService]);

    /**
     * customer-Details Data Service
     *
     * @returns {{get: Function}}
     */
    function CustomerService($q) {
        return {
            get : function(customerId) {
                var data = {"name": "User1", "userId": customerId, "key2":"value2", "key3":"value3","key4":"..."};
                //var data = $http.get('/classifications/' + customerId);
                return $q.when(data);
            }
        };
    }

})();
