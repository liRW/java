# swagger 注解使用
*@ApiIgnore
忽略暴露的 api
*@ApiOperation
@ApiOperation(value = "查找", notes = "根据用户 ID 查找用户") 
*@Api：
@Api：用在类上，说明该类的作用
##@ApiImplicitParams：
@ApiImplicitParams：用在方法上包含一组参数说明
##@ApiImplicitParam
@ApiImplicitParam：用在@ApiImplicitParams注解中，指定一个请求参数的各个方面<br/> 
paramType：参数放在哪个地方<br/> 
name：参数代表的含义<br/> 
value：参数名称<br/> 
dataType： 参数类型，有String/int，无用<br/> 
required ： 是否必要<br/> 
defaultValue：参数的默认值<br/> 
##@ApiResponses：
@ApiResponses：用于表示一组响应 
##@ApiResponse：
@ApiResponse：用在@ApiResponses 中，一般用于表达一个错误的响应信息 <br/> 
code：数字，例如 400<br/> 
message：信息，例如"请求参数没填好" <br/> 
response：抛出异常的类
##@ApiModel：
@ApiModel：描述一个 Model 的信息（这种一般用在 post 创建的时候，使用@RequestBody 这样的场
景，请求参数无法使用@ApiImplicitParam 注解进行描述的时候）
##@ApiModelProperty：
@ApiModelProperty：描述一个 model 的属性
##访问连接
ip:端口/swagger-ui.html
##参考文档
http://blog.csdn.net/u014231523/article/details/76522486
http://blog.csdn.net/qq_25615395/article/details/70229139