(function(){

    angular
        .module('MDC')
        .controller('UserController', [
            'userService', '$log',
            UserController
        ]);

    /**
     * Main Controller for the Angular Material Starter App
     * @param $scope
     * @param $mdSidenav
     * @param avatarsService
     * @constructor
     */
    function UserController(userService, $log ) {
        var self = this;

        self.users        = [ ];

        userService
            .loadAllUsers()
            .then( function( users ) {
                $log.debug(users.size() + " users loaded");
                self.users    = [].concat(users);
            });
    }

})();
