(function(){

    angular
        .module('MDC')
        .controller('UserController', [
            'userService', '$stateParams', '$log',
            UserController
        ]);


    /**
     * Manages details for a specific user
     */
    function UserController(userService, $stateParams, $log ) {
        var self = this;

        self.user        = { };
        self.userId = $stateParams.userId;

        userService
            .get(self.userId)
            .then( function( user ) {
                $log.debug("User " + self.userId + "'s details loaded");
                self.user    = user;
            });

        self.tags = ['Amazon', 'PayPal', '...'];
    }

})();
