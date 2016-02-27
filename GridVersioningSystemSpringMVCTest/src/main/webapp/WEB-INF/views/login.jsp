<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="head.jsp" %>
<style>
.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}

#login-box {
	max-width: 400px;
	padding: 20px;
	margin: auto;
	margin-top: 30px;
	text-align: center;
	background: #fff;
	-webkit-border-radius: 10px;
    -moz-border-radius: 10px;
    border-radius: 10px;
    border: 1px solid grey;
}

#inner{
	margin:auto;
}
</style>
  <body>


    <body onload='document.loginForm.username.focus();'>

	
<div class="container">
	<div id="login-box">
		<div id="inner">
			
	
			<c:if test="${not empty error}">
				<div class="error">${error}</div>
			</c:if>
			<c:if test="${not empty msg}">
				<div class="msg">${msg}</div>
			</c:if>
			
			<img style="max-width:100%" id="headim" alt="" src="resources/gqmlogo.png">
			<h2>Grid Versioning System</h2>
			      <form class="form-signin" action="<c:url value='j_spring_security_check' />" method='POST'>
			        <h2 class="form-signin-heading"><small>Please sign in</small></h2>
			        <label for="inputEmail" class="sr-only">Email address</label>
			        <input style="margin-bottom: 5px;" type="email" name="username" class="form-control" placeholder="Email address" required autofocus>
			        <label for="inputPassword" class="sr-only">Password</label>
			        <input style="margin-bottom: 5px;" type="password" name ="password" class="form-control" placeholder="Password" required>
			        <input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
			        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
			      </form>
			      </div>
		</div>
	</div>

</body>


<%@ include file="footer.jsp" %>

   	 
</html>
