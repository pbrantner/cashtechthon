(function(){

    angular
        .module('MDC')
        .controller('UserController', [
            'userService', '$routeParams', '$log',
            UserController
        ]);


    /**
     * Manages details for a specific user
     */
    function UserController(userService, $routeParams, $log ) {
        var self = this;

        self.user        = [ ];
        self.userId = $routeParams.userId;

        userService
            .get(self.userId)
            .then( function( user ) {
                $log.debug("User " + self.userId + "'s details loaded");
                self.user    = user;
            });
    }

})();
