(function(){
    'use strict';

    angular.module('MDC',[]);

    angular.module('MDC')
        .service('mainService', ['$q', MainService]);

    /**
     * Main Data Service
     *
     * @returns {{loadAll: Function}}
     */
    function MainService($q) {
        return {
            loadAllUsers : function() {
                var users = [{"name": "User1"}, {"name":"User2"}];
                //var users = $http.get('/someUrl');

                return $q.when(users);
            }
        };
    }

})();
