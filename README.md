API文件請查看URL:http://35.206.245.128:8081/swagger-ui/index.html?url=/mall

以下API不需要登入就能呼叫

"/members/register"       註冊會員帳號

"/members/login"          帳號登入

"/members/forgetPassword" 忘記密碼

有開啟CSRF保護除了Get呼叫，請先使用帳號登入API，取得XSRF-TOKEN
![圖片](https://github.com/Hugo-Fan/Hugodemo-mall/assets/163747982/8bcc5099-d3cc-4edd-9a12-5c2cc523cd86)

