(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(tvConfig);

  function tvConfig($stateProvider) {
    $stateProvider
      .state('tv', {
        abstract: true,
        parent: 'site'
      });
  }

})();
