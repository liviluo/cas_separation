# 前后端分离项目使用CAS认证解决方案

### 环境
jdk11+，maven3+，node10+

### 技术
前端：vue3（vue-router、vuex、axios、js-cookie）  
后端：springboot2.6.（cas-client）

### 运行项目
1. 前端/cas_separation_frontend
```
npm install
npm run serve
```

2. 后端/cas_separation_backend
```
mvn clean package
cd target
java -jar cas_separation_backend-0.0.1-SNAPSHOT.war
```

### 说明
此方案只适用于js可读取cookie的情况，如果是HttpOnly则无法通过此方案实现

### 认证流程
1. 客户端：浏览器向前端服务发起请求
2. 前端：router拦截到请求，判断token（cookie：JSESSONID）不存在，重定向到后端auth/cas接口
3. 后端：auth/cas接到请求后，重定向到cas服务端做认证
4. cas：认证完成后，生成ticket，并重定向回到后端auth/login接口，
5. 后端：过滤器拦截到访问auth/login的请求，检测到有ticket，验证ticket并创建session
6. 后端：session创建完成后，重定向回前端，并设置token（cookie：JSESSONID）
7. 前端：router拦截到请求，判断token（cookie：JSESSONID）是否存在
8. 前端：存在token（cookie：JSESSONID），继续判断user info是否存在
9. 前端：不存在user info，发异步请求到后端auth/user获取用户信息
10. 后端：过滤器拦截到访问auth/user的请求，检测到token（cookie：JSESSONID）有效，通过并返回用户信息
11. 前端：收到返回的用户信息，把用户信息存储在store，并响应浏览器的访问资源

### session过期或失效
1. 前端：axios向后端发起异步请求
2. 后端：过滤器拦截到请求的token（cookie：JSESSONID）无效（session过期或者不存在），返回401
3. 前端：axios拦截到返回401的错误，销毁token（cookie：JSESSONID）和user info，跳转到前端的/home路径
4. 前端：router拦截到访问跳转到/home的请求，判断token（cookie：JSESSONID）不存在，重定向到后端auth/cas接口
5. 参考上面认证流程的3到11

### 流程图
![输入图片说明](%E5%9B%BE%E7%89%87.png)

### 前端主要配置
- [token（cookie：JSESSIONID）](https://gitee.com/liviluo/cas_separation/blob/master/cas_separation_frontend/src/auth/token.js)
```
import Cookies from 'js-cookie'
const TokenKey = 'JSESSIONID'
export function getToken() {
  return Cookies.get(TokenKey)
}
```

- [router拦截：token](https://gitee.com/liviluo/cas_separation/blob/master/cas_separation_frontend/src/auth/index.js)
```
const whiteList = ['/login', '/logout']
router.beforeEach((to, from, next) => {
  if (store.getters.token) {
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      // 检查用户信息是否存在
      if (store.getters.account === '') {
        // 用户信息不存在，call api获取用户信息
        store.dispatch('GetUser');
        next()
      } else {
        // 用户信息存在，直接进入
        next()
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      // 否则全部重定向到登录页
      next(`/login?redirect=${to.fullPath}`)
    }
  }
})
```

- [axios拦截：401](https://gitee.com/liviluo/cas_separation/blob/master/cas_separation_frontend/src/utils/request.js)
```
axios.interceptors.response.use(res => {
      return Promise.resolve(res);
  },
  error => {
    console.log('error：' + error);
    if (error.message.includes("401")) {
      store.dispatch('LogOut').then(() => {
        location.href = '/home';
      })
      alert('无效的会话，或者会话已过期，请重新登录。');
    }
    return Promise.reject(error.message);
  }
)
```

- [重定向到后端的cas重定向接口](https://gitee.com/liviluo/cas_separation/blob/master/cas_separation_frontend/src/views/auth/login.vue)
```
const redirect = this.$route.query.redirect;
window.location.href = 'http://localhost:9001/auth/cas?currentPath='.concat(redirect);
```

### 后端主要配置
- [cas客户端配置](https://gitee.com/liviluo/base_tools/tree/cas_client/)

- [cas过滤器](https://gitee.com/liviluo/cas_separation/blob/master/cas_separation_backend/src/main/java/com/livi/separation/filter/CasConfiguration.java)
```
@Configuration
public class CasConfiguration extends CasClientConfigurerAdapter {
	@Override
	public void configureAuthenticationFilter(FilterRegistrationBean authenticationFilter) {
		super.configureAuthenticationFilter(authenticationFilter);
		// 配置地址，这里还可以配置很多，例如cas重定向策略等。
		authenticationFilter.getInitParameters().put("ignorePattern", "/auth/logout|/auth/cas");
		authenticationFilter.getInitParameters().put("authenticationRedirectStrategyClass",
				"com.livi.separation.filter.CustomAuthenticationRedirectStrategy");
	}
}
```

- [重写cas重定向策略](https://gitee.com/liviluo/cas_separation/blob/master/cas_separation_backend/src/main/java/com/livi/separation/filter/CustomAuthenticationRedirectStrategy.java)
```

public class CustomAuthenticationRedirectStrategy implements AuthenticationRedirectStrategy {
	@Override
	public void redirect(final HttpServletRequest request, final HttpServletResponse response,
			final String potentialRedirectUrl) throws IOException {
		response.setContentType("application/json");
		response.setStatus(401);
		Map<String, String> res = new HashMap<>(2);
		res.put("code", "402");
		res.put("msg", "auth fail");
		try (final PrintWriter writer = response.getWriter()) {
			writer.write(new ObjectMapper().writeValueAsString(res));
			writer.flush();
		}
	}
}
```

- [AuthController](https://gitee.com/liviluo/cas_separation/blob/master/cas_separation_backend/src/main/java/com/livi/separation/controller/AuthController.java)
```
@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	@Value("${casClientLoginUrl}")
	private String loginUrl;
	@Value("${casClientLogoutUrl}")
	private String logoutUrl;

	@RequestMapping(value = "cas", method = RequestMethod.GET)
	public void cas(HttpServletResponse response, String currentPath) throws IOException {
		response.sendRedirect(loginUrl + currentPath);
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public void login(HttpServletRequest request, HttpServletResponse response, String currentPath) throws IOException {
		AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
		String username = principal.getName();
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		response.sendRedirect(currentPath);
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public void logout(HttpServletRequest request, HttpServletResponse response, String currentPath)
			throws IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		response.sendRedirect(logoutUrl);
	}

	@RequestMapping(value = "user", method = RequestMethod.GET)
	public String user(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (String) session.getAttribute("username");
	}
}
```

-  [application.properties](https://gitee.com/liviluo/cas_separation/blob/master/cas_separation_backend/src/main/resources/application.properties)
```
casClientLoginUrl = http://liviluo.top:17080/cas/login?service=http://localhost:9001/auth/login?currentPath=http://localhost:8080
casClientLogoutUrl = http://liviluo.top:17080/cas/logout?service=http://localhost:8080/logout
```
