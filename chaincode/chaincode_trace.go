package main

import (
	"fmt"
	"strconv"
	"encoding/json"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type TraceChaincode struct {
}

/* 定义用户实体 */
type User struct {
	Name string          //用户名
	Password string      //密码
	Level int  		 //权限等级
	CreateDate string  	 //创建日期
	ModifyDate string  	 //修改日期
	Sort int 		 //排序
}

/* 定义商品实体 */
type Goods struct {
	Id string            //商品id
	Name string          //商品名称
	Price string         //商品价格
	CreateDate string  	 //创建日期
	ModifyDate string    //创建日期
	Sort int          //排序
}

/* 定义物流实体 */
type Logistic struct {
	Id string            //物流id
	GoodsId string       //商品id
	CityName string      //城市名称
	CreateDate string  	 //创建日期
	ModifyDate string    //创建日期
	Sort int          //排序
}

/* 定义序号实体 */
type Sort struct {
	SortKey string       //序号主键
	SortNo int           //序号
}

/* 定义权限等级实体 */
type QX struct {
	Level int            //等级
}

/* 定义返回结果实体 */
type Result struct {
	Status bool       	 //序号主键
	Message string       //序号
	Data string 		 //数据
}

/* 定义分页返回结果实体 */
type PageResult struct {
	Status bool       	 //序号主键
	Message string       //序号
	Data string 		 //数据
	PageNum int          //页码
	PageSize int         //每个个数
	Total int            //总数
}

/* 用户列表key */
var userListKey = "sheep_user_list"

/* 商品列表key */
var goodsListKey = "sheep_goods_list"

/* 查看权限key */
var queryLevelKey = "sheep_query_level"

/* 添加权限key */
var addLevelKey = "sheep_add_level"

/* 修改权限key */
var modifyLevelKey = "sheep_modify_level"

/* 删除权限key */
var deleteLevelKey = "sheep_delete_level"

/* 管理员权限key */
var managerLevelKey = "sheep_manager_level"

/* 合约初始化入口 */
func (t *TraceChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("trace_goods Init")
	_, args := stub.GetFunctionAndParameters()

	var name, password string
	var err error

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting name and password")
	}

	// Initialize the chaincode

	// 初始化管理员
	name = args[0]
	password = args[1]
	manager := User{Name:name, Password:password, Level:666, Sort:0}
	managerbytes,_ := json.Marshal(manager)
	err = stub.PutState(name, managerbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	var users []string
	users = append(users, name)
	usersbytes,_ := json.Marshal(users)
	err = stub.PutState(userListKey, usersbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// 初始化用户序列
	userSortKey := "userSort"
	userSort := Sort{SortKey:userSortKey, SortNo:0}
	userSortbytes,_ := json.Marshal(userSort)
	err = stub.PutState(userSortKey, userSortbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// 初始化商品序列
	goodsSortKey := "goodsSort"
	goodsSort := Sort{SortKey:goodsSortKey, SortNo:0}
	goodsSortbytes,_ := json.Marshal(goodsSort)
	err = stub.PutState(goodsSortKey, goodsSortbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// 初始化查看权限等级
	queryLevel := QX{Level:0}
	queryLevelBytes,_ := json.Marshal(queryLevel)
	err = stub.PutState(queryLevelKey, queryLevelBytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// 初始化添加权限等级
	addLevel := QX{Level:0}
	addLevelBytes,_ := json.Marshal(addLevel)
	err = stub.PutState(addLevelKey, addLevelBytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// 初始化修改权限等级
	modifyLevel := QX{Level:0}
	modifyLevelBytes,_ := json.Marshal(modifyLevel)
	err = stub.PutState(modifyLevelKey, modifyLevelBytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// 初始化删除权限等级
	deleteLevel := QX{Level:0}
	deleteLevelBytes,_ := json.Marshal(deleteLevel)
	err = stub.PutState(deleteLevelKey, deleteLevelBytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// 初始化管理员权限等级
	managerLevel := QX{Level:11}
	managerLevelBytes,_ := json.Marshal(managerLevel)
	err = stub.PutState(managerLevelKey, managerLevelBytes)
	if err != nil {
		return shim.Error(err.Error())
	}
	
	return shim.Success(nil)
}

/* 合约方法执行入口 */
func (t *TraceChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("the_dragon Invoke")
	function, args := stub.GetFunctionAndParameters()
	if function == "invoke" {
		return t.invoke(stub, args)
	} else if function == "login" {
		return t.login(stub, args)
	} else if function == "register" {
		return t.register(stub, args)
	} else if function == "addManagerForExist" {
		return t.addManagerForExist(stub, args)
	} else if function == "addManagerForNotExist" {
		return t.addManagerForNotExist(stub, args)
	} else if function == "addManager" {
		return t.addManager(stub, args)
	} else if function == "setManagerLevel" {
		return t.setManagerLevel(stub, args)
	} else if function == "setAddLevel" {
		return t.setAddLevel(stub, args)
	} else if function == "setModifyLevel" {
		return t.setModifyLevel(stub, args)
	} else if function == "setQueryLevel" {
		return t.setQueryLevel(stub, args)
	} else if function == "setDeleteLevel" {
		return t.setDeleteLevel(stub, args)
	} else if function == "query" {
		return t.query(stub, args)
	} else if function == "queryOtherChannel" {
		return t.queryOtherChannel(stub, args)
	} else if function == "queryWithLevel" {
		return t.queryWithLevel(stub, args)
	} else if function == "queryAllUser" {
		return t.queryAllUser(stub, args)
	} else if function == "queryGoodsById" {
		return t.queryGoodsById(stub, args)
	} else if function == "queryAllGoods" {
		return t.queryAllGoods(stub, args)
	} else if function == "queryAllAddedGoods" {
		return t.queryAllAddedGoods(stub, args)
	} else if function == "queryLogisticByGoodsId" {
		return t.queryLogisticByGoodsId(stub, args)
	} else if function == "addGoods" {
		return  t.addGoods(stub, args)
	} else if function == "modifyGoods" {
		return  t.modifyGoods(stub, args)
	} else if function == "addLogistic" {
		return  t.addLogistic(stub, args)
	} else if function == "modifyLogistic" {
		return t.modifyLogistic(stub, args)
	} else if function == "deleteUser" {
		return t.deleteUser(stub, args)
	} else if function == "deleteGoods" {
		return t.deleteGoods(stub, args)
	} else if function == "deleteLogistic" {
		return t.deleteLogistic(stub, args)
	}

	return shim.Error("Invalid invoke function name."+ function + "Expecting \"invoke\" \"login\" \"register\" \"addManagerForExist\" \"addManagerForNotExist\" \"addManager\" \"setManagerLevel\" \"setAddLevel\" \"setModifyLevel\" \"setQueryLevel\" \"setDeleteLevel\" \"query\" \"queryAllUser\" \"queryGoodsById\" \"queryAllGoods\" \"queryAllAddedGoods\" \"queryLogisticByGoodsId\" \"addGoods\" \"modifyGoods\" \"addLogistic\" \"modifyLogistic\" \"deleteUser\" \"deleteGoods\" \"deleteLogistic\" ")
}

/* 用户登录 */
func (t *TraceChaincode)login(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var name, password string
	var status bool
	var message, data string

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting name, password")
	}

	// Initialize the chaincode
	name = args[0]
	password = args[1]

	userbytes, err := stub.GetState(name)
	if err != nil {
		return shim.Error("Failed to get state")
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if password == user.Password {
		status = true
		message = "登录成功"
		data = string(userbytes)
	} else if userbytes == nil {
		status = false
		message = "用户不存在"
		data = ""
	} else {
		status = false
		message = "用户名或密码错误"
		data = ""
	}

	result := Result{status, message, data}
	
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 用户注册 */
func (t *TraceChaincode)register(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var goods Goods    // Entities
	var name, password, createDate, message, data string
	var err error
	var status bool

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting name, password, createDate")
	}

	name = args[0]
	password = args[1]
	createDate = args[2]

	preuserbytes, err := stub.GetState(name)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if preuserbytes != nil {
		status = false
		message = "该用户已存在"
		data = ""
	} else {
		usersbytes, err := stub.GetState(userListKey)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		var users []string
		err = json.Unmarshal(usersbytes, &users)

		sort := len(users)

		user := User{Name:name, Password:password, Level:0, CreateDate:createDate, ModifyDate:createDate, Sort:sort}
		userbytes,_ := json.Marshal(user)
		err = stub.PutState(name, userbytes)
		if err != nil {
			return shim.Error(err.Error())
		}

		users = append(users, name)
		newusersbytes,_ := json.Marshal(users)
		err = stub.PutState(userListKey, newusersbytes)
		if err != nil {
			return shim.Error(err.Error())
		}
		status = true
		message = "注册成功"
		data = string(newusersbytes)
	}
	
	result := Result{status, message, data}
	rersultbytes,_ := json.Marshal(result)
	return shim.Success(rersultbytes)
}

/* 添加管理员(已注册用户) */
func (t *TraceChaincode)addManagerForExist(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var name, mangerName, managerPwd, modifyDate, message, data string
	var err error
	var status bool

	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Expecting name, managerName, managerPwd, modifyDate")
	}

	name = args[0]
	mangerName = args[1]
	managerPwd = args[2]
	modifyDate = args[3]

	userbytes, err := stub.GetState(name)
	if err != nil {
		return shim.Error("Failed to get state")
	}

	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	managerbytes, err := stub.GetState(mangerName)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if managerbytes == nil {
		status = false
		message = "该超级管理员不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		if manager.Level == 666 {
			managerLevelbytes, err := stub.GetState(managerLevelKey)
			if err != nil {
				return shim.Error("Failed to get state")
			}
			managerLevel := QX{}
			err  = json.Unmarshal(managerLevelbytes, &managerLevel)
			lev := managerLevel.Level

			user := User{}
			err  = json.Unmarshal(userbytes, &user)
			user.Level = lev
			user.ModifyDate = modifyDate
			newuserbytes,_ := json.Marshal(user)
			err = stub.PutState(user.Name, newuserbytes)
			if err != nil {
				return shim.Error(err.Error())
			}
			status = true
			message = "成功添加 " + name + " 为管理员"
			data = string(newuserbytes)
		} else {
			status = false
			message = "权限不足"
			data = ""
		}
	} else {
		status = false
		message = "超级管理员用户名或密码错误"
		data = ""
	}
	
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 添加管理员(未注册用户) */
func (t *TraceChaincode)addManagerForNotExist(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var name, password, mangerName, managerPwd, modifyDate, message, data string
	var err error
	var status bool

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting name, password, managerName, managerPwd, modifyDate")
	}

	name = args[0]
	password = args[1]
	mangerName = args[2]
	managerPwd = args[3]
	modifyDate = args[4]

	userbytes, err := stub.GetState(name)
	if err != nil {
		return shim.Error("Failed to get state")
	}

	if userbytes != nil {
		status = false
		message = "该用户已存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	managerbytes, err := stub.GetState(mangerName)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if managerbytes == nil {
		status = false
		message = "该管理员不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		if manager.Level == 666 {
			managerLevelbytes, err := stub.GetState(managerLevelKey)
			if err != nil {
				return shim.Error("Failed to get state")
			}
			managerLevel := QX{}
			err  = json.Unmarshal(managerLevelbytes, &managerLevel)
			lev := managerLevel.Level

			usersbytes, err := stub.GetState(userListKey)
			if err != nil {
				return shim.Error("Failed to get state")
			}
			var users []string
			err = json.Unmarshal(usersbytes, &users)

			sort := len(users)

			user := User{Name:name, Password:password, Level:lev, CreateDate:modifyDate, ModifyDate:modifyDate, Sort:sort}
			userbytes,_ := json.Marshal(user)
			err = stub.PutState(name, userbytes)
			if err != nil {
				return shim.Error(err.Error())
			}

			users = append(users, name)
			newusersbytes,_ := json.Marshal(users)
			err = stub.PutState(userListKey, newusersbytes)
			if err != nil {
				return shim.Error(err.Error())
			}

			status = true
			message = "成功添加 " + name + " 为管理员, 密码为：" + password
			data = string(newusersbytes)
		} else {
			status = false
			message = "权限不足"
			data = ""
		}
	} else {
		status = false
		message = "管理员用户名或密码错误"
		data = ""
	}
	
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 给用户设置权限等级 */
func (t *TraceChaincode)addManager(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var name, mangerName, managerPwd, level, modifyDate, message, data string
	var err error
	var status bool

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting name, managerName, managerPwd, level, modifyDate")
	}

	name = args[0]
	mangerName = args[1]
	managerPwd = args[2]
	level = args[3]
	modifyDate = args[4]

	userbytes, err := stub.GetState(name)
	if err != nil {
		return shim.Error("Failed to get state")
	}

	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	managerbytes, err := stub.GetState(mangerName)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if managerbytes == nil {
		status = false
		message = "该管理员不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		managerLevelbytes, err := stub.GetState(managerLevelKey)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		managerLevel := QX{}
		err  = json.Unmarshal(managerLevelbytes, &managerLevel)
		lev := managerLevel.Level
		if manager.Level >= lev {
			var newlevel int
			newlevel, err = strconv.Atoi(level)
			if newlevel > manager.Level {
				status = false
				message = "设置的权限等级不能超过自己的权限等级"
				data = ""
			} else {
				user := User{}
				err  = json.Unmarshal(userbytes, &user)

				if newlevel >= manager.Level {
					status = false
					message = "您没有权限设置该用户的权限"
					data = ""
				} else {
					user.Level = newlevel
					user.ModifyDate = modifyDate
					newuserbytes,_ := json.Marshal(user)
					err = stub.PutState(name, newuserbytes)
					if err != nil {
						return shim.Error(err.Error())
					}
					status = true
					message = "成功设置 " + name + " 的权限为" + level
					data = string(newuserbytes)
				}
			}
		} else {
			status = true
			message = "权限不足"
			data = ""
		}
	} else {
		status = false
		message = "管理员用户名或密码错误"
		data = ""
	}
	
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 设置管理员权限等级 */
func (t *TraceChaincode)setManagerLevel(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var mangerName, managerPwd, level, message, data string
	var err error
	var status bool

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting managerName, managerPwd, level")
	}

	mangerName = args[0]
	managerPwd = args[1]
	level = args[2]

	managerbytes, err := stub.GetState(mangerName)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if managerbytes == nil {
		status = false
		message = "该超级管理员不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		if manager.Level == 666 {
			lev,_ := strconv.Atoi(level)
			managerLevel := QX{Level: lev}
			levelbytes,_ := json.Marshal(managerLevel)
			err = stub.PutState(managerLevelKey, levelbytes)
			if err != nil {
				return shim.Error(err.Error())
			}
			status = true
			message = "成功设置管理员权限等级为:" + level
			data = level
		} else {
			status = false
			message = "权限不足"
			data = ""
		}
	} else {
		status = false
		message = "超级管理员用户名或密码错误"
		data = ""
	}
	
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 设置添加权限 */
func (t *TraceChaincode)setAddLevel(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var mangerName, managerPwd, level, message, data string
	var err error
	var status bool

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting managerName, managerPwd, level")
	}

	mangerName = args[0]
	managerPwd = args[1]
	level = args[2]

	managerbytes, err := stub.GetState(mangerName)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if managerbytes == nil {
		status = false
		message = "该管理员不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		managerLevelbytes, err := stub.GetState(managerLevelKey)
		if err != nil {
			return shim.Error("Failed to get state")
		}

		managerLevel := QX{}
		err  = json.Unmarshal(managerLevelbytes, &managerLevel)
		lev := managerLevel.Level
		if manager.Level >= lev {
			var newlevel int
			newlevel, err = strconv.Atoi(level)
			if newlevel > lev {
				status = false
				message = "设置的权限等级不能超过管理员权限等级"
				data = ""
			} else {
				addLevel := QX{Level: newlevel}
				addLevelBytes,_ := json.Marshal(addLevel)
				err = stub.PutState(addLevelKey, addLevelBytes)
				if err != nil {
					return shim.Error(err.Error())
				}
				status = true
				message = "成功设置添加权限为:" + level
				data = level
			}
		} else {
			status = true
			message = "权限不足"
			data = ""
		}
	} else {
		status = false
		message = "管理员用户名或密码错误"
		data = ""
	}
	
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 设置修改权限 */
func (t *TraceChaincode)setModifyLevel(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var mangerName, managerPwd, level, message, data string
	var err error
	var status bool

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting managerName, managerPwd, level")
	}

	mangerName = args[0]
	managerPwd = args[1]
	level = args[2]

	managerbytes, err := stub.GetState(mangerName)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if managerbytes == nil {
		status = false
		message = "该管理员不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		managerLevelbytes, err := stub.GetState(managerLevelKey)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		managerLevel := QX{}
		err  = json.Unmarshal(managerLevelbytes, &managerLevel)
		lev := managerLevel.Level
		if manager.Level >= lev {
			var newlevel int
			newlevel, err = strconv.Atoi(level)
			if newlevel > lev {
				status = false
				message = "设置的权限等级不能超过管理员权限等级"
				data = ""
			} else {
				modifyLevel := QX{Level: newlevel}
				modifyLevelBytes,_ := json.Marshal(modifyLevel)
				err = stub.PutState(modifyLevelKey, modifyLevelBytes)
				if err != nil {
					return shim.Error(err.Error())
				}
				status = true
				message = "成功设置修改权限为:" + level
				data = level
			}
		} else {
			status = true
			message = "权限不足"
			data = ""
		}
	} else {
		status = false
		message = "管理员用户名或密码错误"
		data = ""
	}
	
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 设置查看权限 */
func (t *TraceChaincode)setQueryLevel(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var mangerName, managerPwd, level, message, data string
	var err error
	var status bool

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting managerName, managerPwd, level")
	}

	mangerName = args[0]
	managerPwd = args[1]
	level = args[2]

	managerbytes, err := stub.GetState(mangerName)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if managerbytes == nil {
		status = false
		message = "该管理员不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		managerLevelbytes, err := stub.GetState(managerLevelKey)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		managerLevel := QX{}
		err  = json.Unmarshal(managerLevelbytes, &managerLevel)
		lev := managerLevel.Level
		if manager.Level >= lev {
			var newlevel int
			newlevel, err = strconv.Atoi(level)
			if newlevel > lev {
				status = false
				message = "设置的查看权限等级不能超过管理员权限等级"
				data = ""
			} else {
				queryLevel := QX{Level: newlevel}
				queryLevelBytes,_ := json.Marshal(queryLevel)
				err = stub.PutState(queryLevelKey, queryLevelBytes)
				if err != nil {
					return shim.Error(err.Error())
				}
				status = true
				message = "成功设置查看权限为:" + level
				data = level
			}
		} else {
			status = true
			message = "权限不足"
			data = ""
		}
	} else {
		status = false
		message = "管理员用户名或密码错误"
		data = ""
	}
	
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 设置删除权限 */
func (t *TraceChaincode)setDeleteLevel(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var mangerName, managerPwd, level, message, data string
	var err error
	var status bool

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting managerName, managerPwd, level")
	}

	mangerName = args[0]
	managerPwd = args[1]
	level = args[2]

	managerbytes, err := stub.GetState(mangerName)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if managerbytes == nil {
		status = false
		message = "该管理员不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		managerLevelbytes, err := stub.GetState(managerLevelKey)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		managerLevel := QX{}
		err  = json.Unmarshal(managerLevelbytes, &managerLevel)
		lev := managerLevel.Level
		if manager.Level >= lev {
			var newlevel int
			newlevel, err = strconv.Atoi(level)
			if newlevel > lev {
				status = false
				message = "设置的权限等级不能超过管理员权限等级"
				data = ""
			} else {
				deleteLevel := QX{Level: newlevel}
				deleteLevelBytes,_ := json.Marshal(deleteLevel)
				err = stub.PutState(deleteLevelKey, deleteLevelBytes)
				if err != nil {
					return shim.Error(err.Error())
				}
				status = true
				message = "成功设置删除权限为:" + level
				data = level
			}
		} else {
			status = true
			message = "权限不足"
			data = ""
		}
	} else {
		status = false
		message = "管理员用户名或密码错误"
		data = ""
	}
	
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}


/* 添加商品信息 */
func (t *TraceChaincode)addGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var goods Goods    // Entities
	var id, name, price, createDate, username string
	var err error
	notExist := true

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting id, name, price, createDate, username")
	}

	// Initialize the chaincode
	id = args[0]
	name = args[1]
	price = args[2]
	createDate = args[3]
	username = args[4]

	var message, data string
	var status bool

	addLevelbytes, err := stub.GetState(addLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	addLevel := QX{}
	err  = json.Unmarshal(addLevelbytes, &addLevel)
	lev := addLevel.Level
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = "添加权限不足"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	goodsInfo, err := stub.GetState(id)
	if err != nil {
		return shim.Error("Failed to get state")
	}

	if goodsInfo != nil {
		notExist = false
	}

	goodsSortKey := "goodsSort"
	goodsSortbytes, err := stub.GetState(goodsSortKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	var i int
	goodsSortEntity := Sort{}
	err  = json.Unmarshal(goodsSortbytes, &goodsSortEntity)
	i = goodsSortEntity.SortNo
	i++

	newgoodsSortEntity := Sort{SortKey:goodsSortKey, SortNo:i}
	newgoodsSortbytes,_ := json.Marshal(newgoodsSortEntity)
	err = stub.PutState(goodsSortKey, newgoodsSortbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// Write the state to the ledger
	goods := Goods{Id:id, Name:name, Price:price, CreateDate:createDate, ModifyDate:createDate, Sort:i}
	goodsbytes,_ := json.Marshal(goods)
	err = stub.PutState(id, goodsbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	logisticSortKey := id + "sort"
	j := 0
	logisticSort := Sort{SortKey:logisticSortKey, SortNo:j}
	logisticSortbytes,_ := json.Marshal(logisticSort)
	err = stub.PutState(logisticSortKey, logisticSortbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	status = true
	message = "添加商品成功"
	data = string(goodsbytes)

	if notExist {
		var goodsIds []string
		goodsIdsBytes, err := stub.GetState(goodsListKey)
		if err != nil {
			jsonResp := "{\"Error\":\"Failed to get state for " + goodsListKey + "\"}"
			return shim.Error(jsonResp)
		}

		if goodsbytes != nil {
			err = json.Unmarshal(goodsIdsBytes, &goodsIds)
		}

		goodsIds = append(goodsIds, id)

		newgoodsIdsBytes,_ := json.Marshal(goodsIds)
		err = stub.PutState(goodsListKey, newgoodsIdsBytes)
	}
	
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 修改商品信息 */
func (t *TraceChaincode)modifyGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var goods Goods    // Entities
	var id, name, price, modifyDate, username string
	var err error

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting id, name, price, modifyDate, username")
	}

	id = args[0]
	name = args[1]
	price = args[2]
	modifyDate = args[3]
	username = args[4]

	var message, data string
	var status bool

	modifyLevelbytes, err := stub.GetState(modifyLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	modifyLevel := QX{}
	err  = json.Unmarshal(modifyLevelbytes, &modifyLevel)
	lev := modifyLevel.Level
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = "修改权限不足"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	goodsbytes, err := stub.GetState(id)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if goodsbytes == nil {
		status = false
		message = "该商品不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	goods := Goods{}
	err  = json.Unmarshal(goodsbytes, &goods)

	goods.Name = name
	goods.Price = price
	goods.ModifyDate = modifyDate

	newgoodsbytes,_ := json.Marshal(goods)
	err = stub.PutState(id, newgoodsbytes)
	if err != nil {
		return shim.Error(err.Error())
	}
	status = true
	message = "修改商品成功"
	data = string(goodsbytes)
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 添加物流信息 */
func (t *TraceChaincode)addLogistic(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var logistic Logistic    // Entities
	var id, goodsId, cityName, createDate, username string
	var err error

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting id, goodsId, cityName, sort, username, createDate")
	}

	// Initialize the chaincode
	id = args[0]
	goodsId = args[1]
	cityName = args[2]
	createDate = args[3]
	username = args[4]

	var message, data string
	var status bool

	addLevelbytes, err := stub.GetState(addLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	addLevel := QX{}
	err  = json.Unmarshal(addLevelbytes, &addLevel)
	lev := addLevel.Level
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = "添加权限不足"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	sortKey := goodsId + "sort"
	sortbytes, err := stub.GetState(sortKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	sortEntity := Sort{}
	err  = json.Unmarshal(sortbytes, &sortEntity)
	i := sortEntity.SortNo
	i++

	newsortEntity := Sort{SortKey:sortKey, SortNo:i}
	newsortbytes,_ := json.Marshal(newsortEntity)
	err = stub.PutState(sortKey, newsortbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// Write the state to the ledger
	logistic := Logistic{Id:id, GoodsId:goodsId, CityName:cityName, CreateDate:createDate, ModifyDate:createDate, Sort:i}

	key, err := stub.CreateCompositeKey("Goods~Logistic:", []string{goodsId, id})
	if err != nil {
		return shim.Error(err.Error())
	}

	logisticbytes,_ := json.Marshal(logistic)
	err = stub.PutState(key, logisticbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	status = true
	message = "添加物流信息成功"
	data = string(logisticbytes)
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 修改物流信息 */
func (t *TraceChaincode)modifyLogistic(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var logistic Logistic    // Entities
	var id, goodsId, cityName, username, modifyDate string
	var err error

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting id, goodsId, cityName, username, modifyDate")
	}

	id = args[0]
	goodsId = args[1]
	cityName = args[2]
	username = args[3]
	modifyDate = args[4]

	var message, data string
	var status bool

	modifyLevelbytes, err := stub.GetState(modifyLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	modifyLevel := QX{}
	err  = json.Unmarshal(modifyLevelbytes, &modifyLevel)
	lev := modifyLevel.Level
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = "修改权限不足"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	// Write the state to the ledger
	logistic := Logistic{}

	key, err := stub.CreateCompositeKey("Goods~Logistic:", []string{goodsId, id})
	if err != nil {
		return shim.Error(err.Error())
	}

	logisticbytes,err := stub.GetState(key)
	if err != nil {
		return shim.Error(err.Error())
	}

	if logisticbytes == nil {
		status = false
		message = "该物流信息不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	err = json.Unmarshal(logisticbytes, &logistic)

	logistic.CityName = cityName
	logistic.ModifyDate = modifyDate

	newlogisticbytes,_ := json.Marshal(logistic)
	err = stub.PutState(key, newlogisticbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	status = true
	message = "修改物流信息成功"
	data = string(logisticbytes)
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* invoke方法 */
func (t *TraceChaincode) invoke(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	
	return shim.Success(nil)
}

/* 查看(带权限) */
func (t *TraceChaincode) queryWithLevel(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var key, username, message, data string
	var status bool

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting key and username")
	}

	key = args[0]
	username = args[1]

	managerLevelbytes, err := stub.GetState(managerLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	managerLevel := QX{}
	err  = json.Unmarshal(managerLevelbytes, &managerLevel)
	lev := managerLevel.Level
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = "查看权限不足"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	keybytes, err := stub.GetState(key)
	if err != nil {
		shim.Error(err.Error())
	}

	status = true
	message = "查询成功"
	data = string(keybytes)

	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 查看（不带权限）*/
func (t *TraceChaincode) query(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var key, message, data string
	var status bool

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting key")
	}

	key = args[0]

	keybytes, err := stub.GetState(key)
	if err != nil {
		shim.Error(err.Error())
	}

	status = true
	message = "查询成功"
	data = string(keybytes)

	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 查看其他通道的智能合约（不带权限）*/
func (t *TraceChaincode) queryOtherChannel(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var chaincodeName, channelName, key, message, data string
	var status bool

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting chaincodeName, channelName and key")
	}

	chaincodeName = args[0]
	channelName = args[1]
	key = args[2]

	parmas := []string{"query", key}
	queryArgs := make([][]byte, len(parmas))
	for i, arg := range parmas {
		queryArgs[i] = []byte(arg)
	}

	response := stub.InvokeChaincode(chaincodeName, queryArgs, channelName)

	if response.Status != shim.OK {
		errStr := fmt.Sprintf("Failed to query chaincode. Got error: %s", response.Payload)
		status = false
		message = "查询失败"
		data = errStr
	} else {
		status = true
		message = "查询成功"
		data = string(response.Payload)
	}

	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 查看所有用户信息 */
func (t *TraceChaincode) queryAllUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var username, pageNum, pageSize string

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting username, pageNum, pageSize")
	}

	username = args[0]
	pageNum = args[1]
	pageSize = args[2]

	var message, data string
	var num, size, total int
	var status bool

	managerLevelbytes, err := stub.GetState(managerLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	managerLevel := QX{}
	err  = json.Unmarshal(managerLevelbytes, &managerLevel)
	lev := managerLevel.Level
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = "查看用户权限不足"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	usersbytes, err := stub.GetState(userListKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	var users []string
	err = json.Unmarshal(usersbytes, &users)

	num,_ = strconv.Atoi(pageNum)
	size,_ = strconv.Atoi(pageSize)
	total = len(users)
	start := (num - 1) * size
	end := num * size - 1
	if end >= total {
		end = total - 1
	}

	var pageUsers []string

	if total > start {
		for i:=start;i<=end;i++ {
			pageUsers = append(pageUsers,users[i])
		}
	}

	userMap := []User{}
	for _, _name := range pageUsers {

		userbytes, err := stub.GetState(_name)
		if err != nil {
			jsonResp := "{\"Error\":\"Failed to get state for " + _name + "\"}"
			return shim.Error(jsonResp)
		}

		if userbytes == nil {
			jsonResp := "{\"Error\":\"Nil amount for " + _name + "\"}"
			return shim.Error(jsonResp)
		}

		user := User{}
	   	err  = json.Unmarshal(userbytes, &user)
		if err != nil {
   			return shim.Error(err.Error())
   		}

		userMap = append(userMap, user)
	}
	usersJson, err := json.Marshal(userMap)
	if err != nil {
		shim.Error("Failed to decode json of productMap")
	}

	status = true
	message = "查询成功"
	data = string(usersJson)
	result := PageResult{Status: status, Message: message, Data: data, PageNum: num, PageSize: size, Total: total}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 查看所有商品信息 */
func (t *TraceChaincode) queryAllGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var username, pageNum, pageSize string

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting username, pageNum, pageSize")
	}

	username = args[0]
	pageNum = args[1]
	pageSize = args[2]

	var message, data string
	var total, num, size int
	var status bool

	queryLevelbytes, err := stub.GetState(queryLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	queryLevel := QX{}
	err  = json.Unmarshal(queryLevelbytes, &queryLevel)
	lev := queryLevel.Level
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = "查看权限不足"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	goodsIdsBytes, err := stub.GetState(goodsListKey)
	if err != nil {
		shim.Error(err.Error())
	}
	
	var goodsIds []string

	err = json.Unmarshal(goodsIdsBytes, &goodsIds)

	num,_ = strconv.Atoi(pageNum)
	size,_ = strconv.Atoi(pageSize)
	total = len(goodsIds)
	start := (num - 1) * size
	end := num * size - 1
	if end >= total {
		end = total - 1
	}

	var pageGoodsIds []string

	if total > start {
		for i:=start;i<=end;i++ {
			pageGoodsIds = append(pageGoodsIds,goodsIds[i])
		}
	}

	goodsMap := []Goods{}
	for _,id := range pageGoodsIds {
		goodsIdbytes, err := stub.GetState(id)
		if err != nil {
			jsonResp := "{\"Error\":\"Failed to get state for " + id + "\"}"
			return shim.Error(jsonResp)
		}

		if goodsIdbytes == nil {
			continue
		}
		goods := Goods{}
	   	err  = json.Unmarshal(goodsIdbytes, &goods)
		if err != nil {
   			return shim.Error(err.Error())
   		}

		goodsMap = append(goodsMap, goods)
	}
	goodsJson, err := json.Marshal(goodsMap)
	if err != nil {
		shim.Error("Failed to decode json of productMap")
	}

	status = true
	message = "查询成功"
	data = string(goodsJson)
	result := PageResult{Status: status, Message: message, Data: data, PageNum: num, PageSize: size, Total: total}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 根据商品id查看商品信息 */
func (t *TraceChaincode) queryGoodsById(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var id,username string

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting goodsId and username")
	}

	id = args[0]
	username = args[1]

	var message, data string
	var status bool

	queryLevelbytes, err := stub.GetState(queryLevelKey)
	if err != nil {
		status = false
		message = "查询 查询权限等级失败"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}
	queryLevel := QX{}
	err  = json.Unmarshal(queryLevelbytes, &queryLevel)
	lev := queryLevel.Level
	userbytes, err := stub.GetState(username)
	if err != nil {
		status = false
		message = "查询失败"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}
	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = "查看权限不足"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	goodsbytes, err := stub.GetState(id)
	if err != nil {
		status = false
		message = "查询失败"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	if goodsbytes == nil {
		status = false
		message = "未查询到该商品"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	status = true
	message = "查询成功"
	data = string(goodsbytes)
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 查看所有商品(包括已删除的)信息 */
func (t *TraceChaincode) queryAllAddedGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var username string

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting username")
	}

	username = args[0]

	var message, data string
	var status bool

	queryLevelbytes, err := stub.GetState(queryLevelKey)
	if err != nil {
		status = false
		message = "查询 查询权限等级失败"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}
	queryLevel := QX{}
	err  = json.Unmarshal(queryLevelbytes, &queryLevel)
	lev := queryLevel.Level
	userbytes, err := stub.GetState(username)
	if err != nil {
		status = false
		message = "查询失败"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}
	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = "查看权限不足"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	goodsIdsBytes, err := stub.GetState(goodsListKey)
	if err != nil {
		shim.Error(err.Error())
	}
	
	goodsIds := make(map[string]string)

	err = json.Unmarshal(goodsIdsBytes, &goodsIds)

	goodsMap := []Goods{}
	for id, _id := range goodsIds {

		goodsIdbytes, err := stub.GetState(id)
		if err != nil {
			jsonResp := "{\"Error\":\"Failed to get state for " + id + _id + "\"}"
			return shim.Error(jsonResp)
		}

		if goodsIdbytes == nil {
			jsonResp := "{\"Error\":\"Nil amount for " + id + _id + "\"}"
			return shim.Error(jsonResp)
		}

		goods := Goods{}
	   	err  = json.Unmarshal(goodsIdbytes, &goods)
		if err != nil {
   			return shim.Error(err.Error())
   		}

		goodsMap = append(goodsMap, goods)
	}
	goodsJson, err := json.Marshal(goodsMap)
	if err != nil {
		shim.Error("Failed to decode json of productMap")
	}

	status = true
	message = "查询成功"
	data = string(goodsJson)
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 根据商品id查看商品的物流信息 */
func (t *TraceChaincode) queryLogisticByGoodsId(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var id,username string

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting id and username")
	}

	id = args[0]
	username = args[1]

	var message, data string
	var status bool

	queryLevelbytes, err := stub.GetState(queryLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	queryLevel := QX{}
	err  = json.Unmarshal(queryLevelbytes, &queryLevel)
	lev := queryLevel.Level
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = "查看权限不足"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	logisticsMap := []Logistic{}
	resultIterator, err := stub.GetStateByPartialCompositeKey("Goods~Logistic:", []string{id})
	defer resultIterator.Close()
	for resultIterator.HasNext() {
		item, _ := resultIterator.Next()
		logisticJsonBytes, err := stub.GetState(item.Key)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		logistic := Logistic{}
	   	err  = json.Unmarshal(logisticJsonBytes, &logistic)
		if err != nil {
   			return shim.Error(err.Error())
   		}

	    logisticsMap = append(logisticsMap, logistic)
	}
	logisticJson, err := json.Marshal(logisticsMap)
	if err != nil {
		shim.Error("Failed to decode json of productMap")
	}

	status = true
	message = "查询成功"
	data = string(logisticJson)
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 删除用户 */
func (t *TraceChaincode) deleteUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var username, managerName string
	var err error

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting username and managerName")
	}

	username = args[0]
	managerName = args[1]

	var message, data string
	var status bool

	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	managerbytes, err := stub.GetState(managerName)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if managerbytes == nil {
		status = false
		message = "该管理员不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if manager.Level != 666 {
		status = false
		message = "删除用户权限不足"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	// Delete the key from the state in ledger

	err = stub.DelState(username)
	if err != nil {
		return shim.Error(err.Error())
	}

	usersbytes, err := stub.GetState(userListKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	var users []string
	err = json.Unmarshal(usersbytes, &users)

	for index,name := range users {
		if username == name {
			users = append(users[:index],users[index+1:]...)
		}
	}

	newusersbytes,_ := json.Marshal(users)
	err = stub.PutState(userListKey, newusersbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	status = true
	message = "删除成功"
	data = string(userbytes)
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 删除商品信息 */
func (t *TraceChaincode) deleteGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var id, username string
	var err error

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting id and username")
	}

	id = args[0]
	username = args[1]

	var message, data string
	var status bool

	deleteLevelbytes, err := stub.GetState(deleteLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	deleteLevel := QX{}
	err  = json.Unmarshal(deleteLevelbytes, &deleteLevel)
	lev := deleteLevel.Level
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = "删除权限不足"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	// Delete the key from the state in ledger
	goodsbytes, err := stub.GetState(id)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if goodsbytes == nil {
		status = false
		message = "该商品不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}
	err = stub.DelState(id)
	if err != nil {
		return shim.Error("Failed to delete state")
	} else {
		var goodsIds []string
		goodsIdsBytes, err := stub.GetState(goodsListKey)
		if err != nil {
			jsonResp := "{\"Error\":\"Failed to get state for " + goodsListKey + "\"}"
			return shim.Error(jsonResp)
		}

		if goodsIdsBytes != nil {
			err = json.Unmarshal(goodsIdsBytes, &goodsIds)
		}

		for index,_id := range goodsIds {
			if id == _id {
				goodsIds = append(goodsIds[:index],goodsIds[index+1:]...)
			}
		}

		newgoodsIdsBytes,_ := json.Marshal(goodsIds)
		err = stub.PutState(goodsListKey, newgoodsIdsBytes)
		if err != nil {
			return shim.Error(err.Error())
		}
		
		status = true
		message = "删除成功"
		data = string(goodsbytes)
	}

	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 删除物流信息 */
func (t *TraceChaincode) deleteLogistic(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var id, goodsId, username string
	var err error

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting id, goodsId and username")
	}

	id = args[0]
	goodsId = args[1]
	username = args[2]

	var message, data string
	var status bool

	deleteLevelbytes, err := stub.GetState(deleteLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	deleteLevel := QX{}
	err  = json.Unmarshal(deleteLevelbytes, &deleteLevel)
	lev := deleteLevel.Level
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = "该用户不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = "删除权限不足"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	// Delete the key from the state in ledger
	key, err := stub.CreateCompositeKey("Goods~Logistic:", []string{goodsId, id})
	if err != nil {
		return shim.Error(err.Error())
	}

	logisticbytes,err := stub.GetState(key)
	if err != nil {
		return shim.Error(err.Error())
	}

	if logisticbytes == nil {
		status = false
		message = "该物流信息不存在"
		data = ""
		result := Result{status, message, data}
		resultbytes,_ := json.Marshal(result)
		return shim.Success(resultbytes)
	}

	err = stub.DelState(key)
	if err != nil {
		return shim.Error(err.Error())
	}

	status = true
	message = "删除成功"
	data = string(logisticbytes)
	result := Result{status, message, data}
	resultbytes,_ := json.Marshal(result)
	return shim.Success(resultbytes)
}

/* 合约入口 */
func main() {
	err := shim.Start(new(TraceChaincode))
	if err != nil {
		fmt.Printf("Error starting Trace chaincode: %s", err)
	}
}
