(function(){

    angular
        .module('MDC')
        .controller('CustomerController', [
            'customerService', '$stateParams', '$log',
            CustomerController
        ]);


    /**
     * Manages details for a specific customer
     */
    function CustomerController(customerService, $stateParams, $log ) {
        var self = this;

        self.customer        = { };
        self.customerId = $stateParams.customerId;

        customerService
            .get(self.customerId)
            .then( function( customer ) {
                $log.debug("customer " + self.customerId + "'s details loaded");
                self.customer    = customer.data;
            });

        self.tags = ['Amazon', 'PayPal', '...'];
    }

})();
