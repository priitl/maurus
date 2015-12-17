(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('TvPopular', TvPopular);

  function TvPopular($resource) {
    return $resource('api/tv/popular', {}, {
      'query': {method: 'GET', isArray: true}
    });
  }

})();
