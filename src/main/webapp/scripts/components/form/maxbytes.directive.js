/* globals $ */
'use strict';
(function () {
  'use strict';

  angular
    .module('maurusApp')
    .directive('maxbytes', maxbytes);

  function maxbytes() {
    return {
      restrict: 'A',
      require: '?ngModel',
      link: link
    };

    function link(scope, element, attrs, ngModel) {
      if (!ngModel) {
        return;
      }

      ngModel.$validators.maxbytes = function (modelValue) {
        return ngModel.$isEmpty(modelValue) || numberOfBytes(modelValue) <= attrs.maxbytes;
      };
    }

    function endsWith(suffix, str) {
      return str.indexOf(suffix, str.length - suffix.length) !== -1;
    }

    function paddingSize(base64String) {
      if (endsWith('==', base64String)) {
        return 2;
      }
      if (endsWith('=', base64String)) {
        return 1;
      }
      return 0;
    }

    function numberOfBytes(base64String) {
      return base64String.length / 4 * 3 - paddingSize(base64String);
    }
  }

})();
