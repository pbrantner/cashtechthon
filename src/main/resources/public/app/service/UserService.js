(function(){
    'use strict';

    angular.module('MDC')
        .service('userService', ['$q', UserService]);

    /**
     * User-Details Data Service
     *
     * @returns {{get: Function}}
     */
    function UserService($q) {
        return {
            get : function(userId) {
                var data = {"name": "User1", "userId": userId, "key2":"value2", "key3":"value3","key4":"..."};
                //var users = $http.get('/someUrl/' + userId);

                return $q.when(data);
            }
        };
    }

})();
