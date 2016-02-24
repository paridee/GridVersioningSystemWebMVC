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
	width: 80%;
	padding: 20px;
	margin: 100px auto;
	text-align: center;
	background: #fff;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border: 1px solid #000;
}

#inner{
	margin:auto;
}
</style>
  <body>

    <%@ include file="navBar.jsp" %>

    <body onload='document.loginForm.username.focus();'>

	<h1>Grid Versioning System - login</h1>
	<h2>Welcome</h2>

	<div id="login-box">
		<div id="inner">
			<h2>Insert your email and your password</h2>
	
			<c:if test="${not empty error}">
				<div class="error">${error}</div>
			</c:if>
			<c:if test="${not empty msg}">
				<div class="msg">${msg}</div>
			</c:if>
			
			<img id="headim" alt="" src="resources/gqmlogo.png">
				<form name='loginForm'
				  action="<c:url value='j_spring_security_check' />" method='POST'>
		
				  <table>
					<tr>
						<td>Email:</td>
						<td><input type='text' name='username' value='paride.casulli@gmail.com'></td>
					</tr>
					<tr>
						<td>Password:</td>
						<td><input type='password' name='password' value='123456' /></td>
					</tr>
					<tr>
						<td colspan='2'><input name="submit" type="submit"
							value="Login" /></td>
					</tr>
				  </table>
		
				  <input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
		
				</form>
		</div>
	</div>

</body>


<%@ include file="footer.jsp" %>
<c:if test="${gridTreeString!=null}">
<!-- Treant javascript -->
    <script src="<c:url value='/resources/Treant/vendor/raphael.js' />"></script>
    <script src="<c:url value='/resources/Treant/Treant.min.js' />"></script>
    <script src="<c:url value='/resources/Treant/vendor/jquery.easing.js' />"></script>
	<script src="<c:url value='/resources/Treant/vendor/perfect-scrollbar/jquery.mousewheel.js' />"></script>
	<script src="<c:url value='/resources/Treant/vendor/perfect-scrollbar/perfect-scrollbar.js' />"></script><script>${gridTreeString};
    	var my_chart = new Treant(chart_config);
   	</script>
</c:if>
<script type="text/javascript">
    jQuery(document).ready(function($) {
        $(".clickable-row").click(function() {
            window.document.location = $(this).data("href");
        });
    });
    </script>
   	 
</html>
