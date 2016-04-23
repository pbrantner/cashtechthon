(function(){

    angular
        .module('MDC')
        .controller('CustomerController', [
            'customerService', 'commonService', '$stateParams', '$log',
            CustomerController
        ]);


    /**
     * Manages details for a specific customer
     */
    function CustomerController(customerService, commonService, $stateParams, $log ) {
        var self = this;

        self.customer        = { };
        self.customerId = $stateParams.customerId;

        var f = commonService.from.getFullYear() + "-" + (commonService.from.getMonth() + 1) + "-" + commonService.from.getDate();
        var t = commonService.till.getFullYear() + "-" + (commonService.till.getMonth() + 1) + "-" + commonService.till.getDate();

        customerService
            .get(self.customerId)
            .then( function( customer ) {
                var test = commonService;
                $log.debug("customer " + self.customerId + "'s details loaded");
                customer.data.avatar = "http://www.gravatar.com/avatar/" + CryptoJS.MD5(customer.data.firstName + " "
                        + customer.data.lastName) + "?s=120&d=identicon";
                self.customer    = customer.data;
            });

        self.tags = ['Amazon', 'PayPal', '...'];

        self.companies = [{
           name: "Amazon",
           icon: "./assets/amazon.png"
        },{
            name: "PayPal",
            icon: "./assets/svg/paypal.svg"
        }];

        self.connections = [{

        }];

        self.planned = [{

        }];
    }

})();
