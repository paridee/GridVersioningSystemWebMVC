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
      height: 50px;
    }
  </style>
</head>

<body>
${pad}
</body>
</html>