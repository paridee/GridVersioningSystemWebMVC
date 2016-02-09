<!doctype html>
<!-- See http://www.firepad.io/docs/ for detailed embedding docs. -->
<html>
<head>
  <meta charset="utf-8" />
  <!-- Firebase -->
  <script src="https://cdn.firebase.com/js/client/2.3.2/firebase.js"></script>

  <!-- CodeMirror and its JavaScript mode file -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.2.0/codemirror.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.2.0/mode/javascript/javascript.js"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.2.0/codemirror.css" />

  <!-- Firepad -->
  <link rel="stylesheet" href="https://cdn.firebase.com/libs/firepad/1.3.0/firepad.css" />
  <script src="https://cdn.firebase.com/libs/firepad/1.3.0/firepad.min.js"></script>

  <style>
    html { height: 100%; }
    body { margin: 0; height: 100%; position: relative; }
    /* Height / width / positioning can be customized for your use case.
       For demo purposes, we make firepad fill the entire browser. */
    .firepad-container {
      width: 100%;
      height: 10%;
    }
  </style>
</head>

<body>
  <div id="primo" class="firepad-container">
  <script>
    function init() {
      //// Initialize Firebase.
      var firepadRef = new Firebase('fiery-torch-6050.firebaseio.com/objectlabel/field1');
      //// Create CodeMirror (with line numbers and the JavaScript mode).
      var codeMirror = CodeMirror(document.getElementById('primo'), {
        lineNumbers: true,
        mode: 'javascript'
      });
      //// Create Firepad.
      var firepad = Firepad.fromCodeMirror(firepadRef, codeMirror, {
        defaultText: 'editor per il primo attributo'
      });
    }
    init();
  </script>
  </div>
  
  <div id="secondo" class="firepad-container">
  <script>
    function init() {
      //// Initialize Firebase.
      var firepadRef = new Firebase('fiery-torch-6050.firebaseio.com/objectlabel/field2');
      //// Create CodeMirror (with line numbers and the JavaScript mode).
      var codeMirror = CodeMirror(document.getElementById('secondo'), {
        lineNumbers: true,
        mode: 'javascript'
      });
      //// Create Firepad.
      var firepad = Firepad.fromCodeMirror(firepadRef, codeMirror, {
        defaultText: 'editor per il secondo attributo'
      });
    }
    init();
  </script>
  </div>
  
  
  
</body>
</html>