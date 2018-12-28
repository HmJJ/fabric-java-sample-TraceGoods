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
type Goods struct {
	Name string          //用户名
	Password string      //密码
	Level string  		 //权限等级
	Sort string 		 //排序
}

/* 定义商品实体 */
type Goods struct {
	Id string            //商品id
	Name string          //商品名称
	Price string         //商品价格
	RegisterDate string  //生产日期
	Sort string          //排序
}

/* 定义物流实体 */
type Logistic struct {
	Id string            //物流id
	GoodsId string       //商品id
	CityName string      //城市名称
	Sort string          //排序
}

/* 定义序号实体 */
type Sort struct {
	SortKey string       //序号主键
	SortNo int          	 //序号
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

	var name, password string
	var err error

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	// Initialize the chaincode

	// 初始化管理员
	name = args[0]
	password = args[1]
	manager := User{Name:name, Password:password, Level:666, Sort:0}
	managerbytes,_ := json.Marshal(manager)
	err = stub.PutState(id, managerbytes)
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
	queryLevel := '0'
	queryLevelBytes,_ := json.Marshal(queryLevel)
	err = stub.PutState(queryLevelKey, queryLevelBytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// 初始化添加权限等级
	addLevel := '0'
	addLevelBytes,_ := json.Marshal(addLevel)
	err = stub.PutState(addLevelKey, addLevelBytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// 初始化修改权限等级
	modifyLevel := '0'
	modifyLevelBytes,_ := json.Marshal(modifyLevel)
	err = stub.PutState(modifyLevelKey, modifyLevelBytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// 初始化删除权限等级
	deleteLevel := '0'
	deleteLevelBytes,_ := json.Marshal(deleteLevel)
	err = stub.PutState(deleteLevelKey, deleteLevelBytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// 初始化管理员权限等级
	managerLevel := '11'
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
		// Make payment of X units from A to B
		return t.invoke(stub, args)
	} else if function == "login" {
		// the old "Query" is now implemtned in invoke
		return t.login(stub, args)
	} else if function == "register" {
		// the old "Query" is now implemtned in invoke
		return t.register(stub, args)
	} else if function == "addManagerForExist" {
		// the old "Query" is now implemtned in invoke
		return t.addManagerForExist(stub, args)
	} else if function == "addManagerForNotExist" {
		// the old "Query" is now implemtned in invoke
		return t.addManagerForNotExist(stub, args)
	} else if function == "addManager" {
		// the old "Query" is now implemtned in invoke
		return t.addManager(stub, args)
	} else if function == "setManagerLevel" {
		// the old "Query" is now implemtned in invoke
		return t.setManagerLevel(stub, args)
	} else if function == "setAddLevel" {
		// the old "Query" is now implemtned in invoke
		return t.setAddLevel(stub, args)
	} else if function == "setModifyLevel" {
		// the old "Query" is now implemtned in invoke
		return t.setModifyLevel(stub, args)
	} else if function == "setQueryLevel" {
		// the old "Query" is now implemtned in invoke
		return t.setQueryLevel(stub, args)
	} else if function == "setDeleteLevel" {
		// the old "Query" is now implemtned in invoke
		return t.setDeleteLevel(stub, args)
	} else if function == "query" {
		// the old "Query" is now implemtned in invoke
		return t.query(stub, args)
	} else if function == "queryAllGoods" {
		// the old "Query" is now implemtned in invoke
		return t.queryAllGoods(stub, args)
	} else if function == "queryAllAddedGoods" {
		// the old "Query" is now implemtned in invoke
		return t.queryAllAddedGoods(stub, args)
	} else if function == "queryLogisticByGoodsId" {
		// the old "Query" is now implemtned in invoke
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
		// Deletes an entity from its state
		return t.deleteUser(stub, args)
	} else if function == "deleteGoods" {
		// Deletes an entity from its state
		return t.deleteGoods(stub, args)
	} else if function == "deleteLogistic" {
		// Deletes an entity from its state
		return t.deleteLogistic(stub, args)
	}

	return shim.Error("Invalid invoke function name."+ function + "Expecting \"invoke\" \"delete\" \"query\" \"queryAllGoods\" \"queryLogisticByGoodsId\" \"addGoods\" \"modifyGoods\" \"addLogistic\" \"modifyLogistic\" \"deleteGoods\" \"deleteLogistic\" ")
}

/* 用户登录 */
func (t *TraceChaincode)login(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var name, password string
	var status bool
	var message string

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
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
		message = '登录成功'
	} else if userbytes == nil {
		status = false
		message = '用户不存在'
	} else {
		status = false
		message = '用户名或密码错误'
	}

	var data [2]string
	data[0] = strconv.FormatBool(status)
	data[1] = message
	
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 用户注册 */
func (t *TraceChaincode)register(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var goods Goods    // Entities
	var name, password, message string
	var data [2]string
	var err error
	var status bool

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	name = args[0]
	password = args[1]

	preuserbytes, err := stub.GetState(name)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if preuserbytes != nil {
		status = false
		message = '该用户已存在'
	} else {
		usersbytes, err := stub.GetState(userListKey)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		var users []string
		users = json.Unmarshal(usersbytes)

		sort := len(users)

		user := User{Name:name, Password:password, Level:0, Sort:sort}
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
		message = '注册成功'
	}
	
	data[0] = strconv.FormatBool(status)
	data[1] = message
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 添加管理员(已注册用户) */
func (t *TraceChaincode)addManagerForExist(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var name, mangerName, managerPwd, message string
	var err error
	var status bool
	var data [2]string

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	name = args[0]
	mangerName = args[1]
	managerPwd = args[2]

	userbytes, err := stub.GetState(name)
	if err != nil {
		return shim.Error("Failed to get state")
	}

	if userbytes == nil {
		status = false
		message = '该用户不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	managerbytes, err := stub.GetState(mangerName)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if managerbytes == nil {
		status = false
		message = '该超级管理员不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		if manager.Level == 666 {
			managerLevelbytes, err := stub.GetState(managerLevelKey)
			if err != nil {
				return shim.Error("Failed to get state")
			}
			lev,_ := strconv.Atoi(string(managerLevelbytes))

			user := User{}
			err  = json.Unmarshal(userbytes, &user)
			user.Level = lev
			newuserbytes,_ := json.Marshal(user)
			err = stub.PutState(user.Name, newuserbytes)
			if err != nil {
				return shim.Error(err.Error())
			}
			status = true
			message = '成功添加 ' + name + ' 为管理员'
			data[0] = strconv.FormatBool(status)
			data[1] = message
		} else {
			status = false
			message = '权限不足'
			data[0] = strconv.FormatBool(status)
			data[1] = message
		}
	} else {
		status = false
		message = '超级管理员用户名或密码错误'
		data[0] = strconv.FormatBool(status)
		data[1] = message
	}
	
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 添加管理员(未注册用户) */
func (t *TraceChaincode)addManagerForNotExist(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var name, password, mangerName, managerPwd, message string
	var err error
	var status bool
	var data [2]string

	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	name = args[0]
	password = args[1]
	mangerName = args[2]
	managerPwd = args[3]

	userbytes, err := stub.GetState(name)
	if err != nil {
		return shim.Error("Failed to get state")
	}

	if userbytes != nil {
		status = false
		message = '该用户已存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	managerbytes, err := stub.GetState(mangerName)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if managerbytes == nil {
		status = false
		message = '该管理员不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		if manager.Level == 666 {
			managerLevelbytes, err := stub.GetState(managerLevelKey)
			if err != nil {
				return shim.Error("Failed to get state")
			}
			lev,_ := strconv.Atoi(string(managerLevelbytes))

			usersbytes, err := stub.GetState(userListKey)
			if err != nil {
				return shim.Error("Failed to get state")
			}
			var users []string
			users = json.Unmarshal(usersbytes)

			sort := len(users)

			user := User{Name:name, Password:password, Level:lev, Sort:sort}
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
			message = '成功添加 ' + name + ' 为管理员, 密码为：' + password
			data[0] = strconv.FormatBool(status)
			data[1] = message
		} else {
			status = false
			message = '权限不足'
			data[0] = strconv.FormatBool(status)
			data[1] = message
		}
	} else {
		status = false
		message = '管理员用户名或密码错误'
		data[0] = strconv.FormatBool(status)
		data[1] = message
	}
	
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 给用户设置权限等级 */
func (t *TraceChaincode)addManager(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var name, mangerName, managerPwd, level, message string
	var err error
	var status bool
	var data [2]string

	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	name = args[0]
	mangerName = args[1]
	managerPwd = args[2]
	level = args[3]

	userbytes, err := stub.GetState(name)
	if err != nil {
		return shim.Error("Failed to get state")
	}

	if userbytes == nil {
		status = false
		message = '该用户不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	managerbytes, err := stub.GetState(mangerName)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if managerbytes == nil {
		status = false
		message = '该管理员不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		managerLevelbytes, err := stub.GetState(managerLevelKey)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		lev,_ := strconv.Atoi(string(managerLevelbytes))
		if manager.Level >= lev {
			var newlevel int
			newlevel, err = strconv.Atoi(level)
			if newlevel > manager.Level {
				status = false
				message = '设置的权限等级不能超过自己的权限等级'
				data[0] = strconv.FormatBool(status)
				data[1] = message
			} else {
				user := User{}
				err  = json.Unmarshal(userbytes, &user)
				user.Level = newlevel
				newuserbytes,_ := json.Marshal(user)
				err = stub.PutState(user.Name, newuserbytes)
				if err != nil {
					return shim.Error(err.Error())
				}
				status = true
				message = '成功设置 ' + name + ' 的权限为' + level
				data[0] = strconv.FormatBool(status)
				data[1] = message
			}
		} else {
			status = true
			message = '权限不足'
			data[0] = strconv.FormatBool(status)
			data[1] = message
		}
	} else {
		status = false
		message = '管理员用户名或密码错误'
		data[0] = strconv.FormatBool(status)
		data[1] = message
	}
	
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 设置管理员权限等级 */
func (t *TraceChaincode)setManagerLevel(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var mangerName, managerPwd, level message string
	var err error
	var status bool
	var data [2]string

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
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
		message = '该超级管理员不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		if manager.Level == 666 {
			levelbytes,_ := json.Marshal(level)
			err = stub.PutState(managerLevelKey, levelbytes)
			if err != nil {
				return shim.Error(err.Error())
			}
			status = true
			message = '成功设置管理员权限等级为:' + level
			data[0] = strconv.FormatBool(status)
			data[1] = message
		} else {
			status = false
			message = '权限不足'
			data[0] = strconv.FormatBool(status)
			data[1] = message
		}
	} else {
		status = false
		message = '超级管理员用户名或密码错误'
		data[0] = strconv.FormatBool(status)
		data[1] = message
	}
	
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 设置添加权限 */
func (t *TraceChaincode)setAddLevel(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var mangerName, managerPwd, level message string
	var err error
	var status bool
	var data [2]string

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
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
		message = '该管理员不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		managerLevelbytes, err := stub.GetState(managerLevelKey)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		lev,_ := strconv.Atoi(string(managerLevelbytes))
		if manager.Level >= lev {
			var newlevel int
			newlevel, err = strconv.Atoi(level)
			if newlevel > lev {
				status = false
				message = '设置的权限等级不能超过管理员权限等级'
				data[0] = strconv.FormatBool(status)
				data[1] = message
			} else {
				addLevelBytes,_ := json.Marshal(level)
				err = stub.PutState(addLevelKey, addLevelBytes)
				if err != nil {
					return shim.Error(err.Error())
				}
				status = true
				message = '成功设置添加权限为:' + level
				data[0] = strconv.FormatBool(status)
				data[1] = message
			}
		} else {
			status = true
			message = '权限不足'
			data[0] = strconv.FormatBool(status)
			data[1] = message
		}
	} else {
		status = false
		message = '管理员用户名或密码错误'
		data[0] = strconv.FormatBool(status)
		data[1] = message
	}
	
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 设置修改权限 */
func (t *TraceChaincode)setModifyLevel(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var mangerName, managerPwd, level message string
	var err error
	var status bool
	var data [2]string

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
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
		message = '该管理员不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		managerLevelbytes, err := stub.GetState(managerLevelKey)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		lev,_ := strconv.Atoi(string(managerLevelbytes))
		if manager.Level >= lev {
			var newlevel int
			newlevel, err = strconv.Atoi(level)
			if newlevel > lev {
				status = false
				message = '设置的权限等级不能超过管理员权限等级'
				data[0] = strconv.FormatBool(status)
				data[1] = message
			} else {
				modifyLevelBytes,_ := json.Marshal(level)
				err = stub.PutState(modifyLevelKey, modifyLevelBytes)
				if err != nil {
					return shim.Error(err.Error())
				}
				status = true
				message = '成功设置修改权限为:' + level
				data[0] = strconv.FormatBool(status)
				data[1] = message
			}
		} else {
			status = true
			message = '权限不足'
			data[0] = strconv.FormatBool(status)
			data[1] = message
		}
	} else {
		status = false
		message = '管理员用户名或密码错误'
		data[0] = strconv.FormatBool(status)
		data[1] = message
	}
	
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 设置查看权限 */
func (t *TraceChaincode)setQueryLevel(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var mangerName, managerPwd, level message string
	var err error
	var status bool
	var data [2]string

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
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
		message = '该管理员不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		managerLevelbytes, err := stub.GetState(managerLevelKey)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		lev,_ := strconv.Atoi(string(managerLevelbytes))
		if manager.Level >= lev {
			var newlevel int
			newlevel, err = strconv.Atoi(level)
			if newlevel > lev {
				status = false
				message = '设置的权限等级不能超过管理员权限等级'
				data[0] = strconv.FormatBool(status)
				data[1] = message
			} else {
				queryLevelBytes,_ := json.Marshal(level)
				err = stub.PutState(queryLevelKey, queryLevelBytes)
				if err != nil {
					return shim.Error(err.Error())
				}
				status = true
				message = '成功设置查看权限为:' + level
				data[0] = strconv.FormatBool(status)
				data[1] = message
			}
		} else {
			status = true
			message = '权限不足'
			data[0] = strconv.FormatBool(status)
			data[1] = message
		}
	} else {
		status = false
		message = '管理员用户名或密码错误'
		data[0] = strconv.FormatBool(status)
		data[1] = message
	}
	
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 设置删除权限 */
func (t *TraceChaincode)setDeleteLevel(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var mangerName, managerPwd, level message string
	var err error
	var status bool
	var data [2]string

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
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
		message = '该管理员不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	manager := User{}
	err  = json.Unmarshal(managerbytes, &manager)

	if managerPwd == manager.Password {
		managerLevelbytes, err := stub.GetState(managerLevelKey)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		lev,_ := strconv.Atoi(string(managerLevelbytes))
		if manager.Level >= lev {
			var newlevel int
			newlevel, err = strconv.Atoi(level)
			if newlevel > lev {
				status = false
				message = '设置的权限等级不能超过管理员权限等级'
				data[0] = strconv.FormatBool(status)
				data[1] = message
			} else {
				deleteLevelBytes,_ := json.Marshal(level)
				err = stub.PutState(deleteLevelKey, deleteLevelBytes)
				if err != nil {
					return shim.Error(err.Error())
				}
				status = true
				message = '成功设置删除权限为:' + level
				data[0] = strconv.FormatBool(status)
				data[1] = message
			}
		} else {
			status = true
			message = '权限不足'
			data[0] = strconv.FormatBool(status)
			data[1] = message
		}
	} else {
		status = false
		message = '管理员用户名或密码错误'
		data[0] = strconv.FormatBool(status)
		data[1] = message
	}
	
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}


/* 添加商品信息 */
func (t *TraceChaincode)addGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var goods Goods    // Entities
	var id, name, price, registerDate, username string
	var err error
	notExist := true

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	// Initialize the chaincode
	id = args[0]
	name = args[1]
	price = args[2]
	registerDate = args[3]
	username = args[4]

	var message string
	var status bool
	var data [2]string

	addLevelbytes, err := stub.GetState(addLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	lev,_ := strconv.Atoi(string(addLevelbytes))
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = '该用户不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = '添加权限不足'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
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
	i := goodsSortEntity.SortNo
	i++
	
	goodsSort := strconv.Itoa(i)

	newgoodsSortEntity := Sort{SortKey:goodsSortKey, SortNo:i}
	newgoodsSortbytes,_ := json.Marshal(newgoodsSortEntity)
	err = stub.PutState(goodsSortKey, newgoodsSortbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// Write the state to the ledger
	goods := Goods{Id:id, Name:name, Price:price, RegisterDate:registerDate, Sort:i}
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

	var dataa [3]string
	dataa[0] = "添加商品成功"
	dataa[1] = string(goodsbytes)

	if notExist {
		goodsIds := make(map[string]string)
		goodsIdsBytes, err := stub.GetState(goodsListKey)
		if err != nil {
			jsonResp := "{\"Error\":\"Failed to get state for " + goodsListKey + "\"}"
			return shim.Error(jsonResp)
		}

		if goodsbytes != nil {
			err = json.Unmarshal(goodsIdsBytes, &goodsIds)
		}

		goodsIds[id] = id

		newgoodsIdsBytes,_ := json.Marshal(goodsIds)
		err = stub.PutState(goodsListKey, newgoodsIdsBytes)
		if err != nil {
			dataa[2] = err.Error()
		} else {
			dataa[2] = strconv.FormatBool(notExist)
		}
	} else {
		dataa[2] = strconv.FormatBool(isExist)
	}
	
	dataabytes,_ := json.Marshal(dataa)
	return shim.Success(dataabytes)
}

/* 修改商品信息 */
func (t *TraceChaincode)modifyGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var goods Goods    // Entities
	var id, name, price, registerDate, username string
	var err error

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	id = args[0]
	name = args[1]
	price = args[2]
	registerDate = args[3]
	username = args[4]

	fmt.Printf("id = %s,goodsName=%s, price = %s, registerDate = %s\n", id, name, price, registerDate)

	var message string
	var status bool
	var data [2]string

	modifyLevelbytes, err := stub.GetState(modifyLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	lev,_ := strconv.Atoi(string(modifyLevelbytes))
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = '该用户不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = '修改权限不足'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	// Write the state to the ledger
	goods := Goods{Id:id, Name:name, Price:price, RegisterDate:registerDate}
	goodsbytes,_ := json.Marshal(goods)
	err = stub.PutState(id, goodsbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	var data [2]string
	data[0] = "修改商品成功"
	data[1] = string(goodsbytes)
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 添加物流信息 */
func (t *TraceChaincode)addLogistic(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var logistic Logistic    // Entities
	var id, goodsId, cityName, sort, username string
	var err error

	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	// Initialize the chaincode
	id = args[0]
	goodsId = args[1]
	cityName = args[2]
	username = args[3]

	fmt.Printf("id = %s,goodsId=%s, price = %s, registerDate = %s\n", id, goodsId, cityName)

	var message string
	var status bool
	var data [2]string

	addLevelbytes, err := stub.GetState(addLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	lev,_ := strconv.Atoi(string(addLevelbytes))
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = '该用户不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = '添加权限不足'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
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
	sort = strconv.Itoa(i)

	newsortEntity := Sort{SortKey:sortKey, SortNo:i}
	newsortbytes,_ := json.Marshal(newsortEntity)
	err = stub.PutState(sortKey, newsortbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	// Write the state to the ledger
	logistic := Logistic{Id:id, GoodsId:goodsId, CityName:cityName, Sort:sort}

	key, err := stub.CreateCompositeKey("Goods~Logistic:", []string{goodsId, id})
	if err != nil {
		return shim.Error(err.Error())
	}

	logisticbytes,_ := json.Marshal(logistic)
	err = stub.PutState(key, logisticbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	var data [2]string
	data[0] = "添加物流信息成功"
	data[1] = string(logisticbytes)
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* 修改物流信息 */
func (t *TraceChaincode)modifyLogistic(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	//var logistic Logistic    // Entities
	var id, goodsId, cityName, sort, username string
	var err error

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	id = args[0]
	goodsId = args[1]
	cityName = args[2]
	sort = args[3]
	username = args[4]

	fmt.Printf("id = %s,goodsId=%s, price = %s, registerDate = %s, sort=%s\n", id, goodsId, cityName, sort)

	var message string
	var status bool
	var data [2]string

	modifyLevelbytes, err := stub.GetState(modifyLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	lev,_ := strconv.Atoi(string(modifyLevelbytes))
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = '该用户不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = '修改权限不足'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	// Write the state to the ledger
	logistic := Logistic{Id:id, GoodsId:goodsId, CityName:cityName, Sort:sort}

	key, err := stub.CreateCompositeKey("Goods~Logistic:", []string{goodsId, id})
	if err != nil {
		return shim.Error(err.Error())
	}

	logisticbytes,_ := json.Marshal(logistic)
	err = stub.PutState(key, logisticbytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	var data [2]string
	data[0] = "修改物流信息成功"
	data[1] = string(logisticbytes)
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)
}

/* invoke方法 */
func (t *TraceChaincode) invoke(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	
	return shim.Success(nil)
}

/* 查看 */
func (t *TraceChaincode) query(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var key string

	key = args[0]

	keybytes, err := stub.GetState(key)
	if err != nil {
		shim.Error(err.Error())
	}

	var data [2]string
	data[0] = "查询成功"
	data[1] = string(keybytes)
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

/* 查看所有商品信息 */
func (t *TraceChaincode) queryAllGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var username string

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	username = args[0]

	var message string
	var status bool
	var data [2]string

	queryLevelbytes, err := stub.GetState(queryLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	lev,_ := strconv.Atoi(string(queryLevelbytes))
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = '该用户不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = '查看权限不足'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	goodsIdsBytes, err := stub.GetState(goodsListKey)
	if err != nil {
		shim.Error(err.Error())
	}
	
	goodsIds := make(map[string]string)

	err = json.Unmarshal(goodsIdsBytes, &goodsIds)

	goodsMap := []Goods{}
	for id, _id := range goodsIds {

		goodsIdbytes, err := stub.GetState(_id)
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

	var data [2]string
	data[0] = "查询成功"
	data[1] = string(goodsJson)
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

/* 查看所有商品(包括已删除的)信息 */
func (t *TraceChaincode) queryAllAddedGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var username string

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	username = args[0]

	var message string
	var status bool
	var data [2]string

	queryLevelbytes, err := stub.GetState(queryLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	lev,_ := strconv.Atoi(string(queryLevelbytes))
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = '该用户不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = '查看权限不足'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
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

	var data [2]string
	data[0] = "查询成功"
	data[1] = string(goodsJson)
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

/* 根据商品id查看商品的物流信息 */
func (t *TraceChaincode) queryLogisticByGoodsId(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var id,username string
	var data [2]string

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	id = args[0]
	username = args[1]

	var message string
	var status bool

	queryLevelbytes, err := stub.GetState(queryLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	lev,_ := strconv.Atoi(string(queryLevelbytes))
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = '该用户不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = '查看权限不足'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	logisticsMap := []Logistic{}
	resultIterator, err := stub.GetStateByPartialCompositeKey("Goods~Logistic:", []string{id})
	defer resultIterator.Close()
	for resultIterator.HasNext() {
		item, _ := resultIterator.Next()
		fmt.Printf("key=%s\n", item.Key)
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

	data[0] = "查询成功"
	data[1] = string(logisticJson)
	databytes,_ := json.Marshal(data)

	return shim.Success(databytes)
}

/* 删除用户 */
func (t *TraceChaincode) deleteUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var username, managerName string
	var err error

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	username = args[0]
	managerName = args[1]

	var message string
	var status bool
	var data [2]string

	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = '该用户不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level != 666 {
		status = false
		message = '删除用户权限不足'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	// Delete the key from the state in ledger

	err = stub.DelState(username)
	if err != nil {
		return shim.Error(err.Error())
	}

	status = true
	message = '删除成功'
	data[0] = strconv.FormatBool(status)
	data[1] = message
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)

	return shim.Success(databytes)
}

/* 删除商品信息 */
func (t *TraceChaincode) deleteGoods(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var id, username string
	var err error

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting name of the person to query")
	}

	id = args[0]
	username = args[1]

	var message string
	var status bool
	var data [2]string

	deleteLevelbytes, err := stub.GetState(deleteLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	lev,_ := strconv.Atoi(string(deleteLevelbytes))
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = '该用户不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = '删除权限不足'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	// Delete the key from the state in ledger
	err = stub.DelState(id)
	if err != nil {
		return shim.Error("Failed to delete state")
	} else {
		goodsIds := make(map[string]string)
		goodsIdsBytes, err := stub.GetState(goodsListKey)
		if err != nil {
			jsonResp := "{\"Error\":\"Failed to get state for " + goodsListKey + "\"}"
			return shim.Error(jsonResp)
		}

		if goodsIdsBytes != nil {
			err = json.Unmarshal(goodsIdsBytes, &goodsIds)
		}

		delete(goodsIds, id)

		newgoodsIdsBytes,_ := json.Marshal(goodsIds)
		err = stub.PutState(goodsListKey, newgoodsIdsBytes)
		if err != nil {
			return shim.Error(err.Error())
		}
		status = true
		message = '删除成功'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	return shim.Success(databytes)
}

/* 删除物流信息 */
func (t *TraceChaincode) deleteLogistic(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var id, goodsId, username string
	var err error

	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	id = args[0]
	goodsId = args[1]
	username = args[2]

	var message string
	var status bool
	var data [2]string

	deleteLevelbytes, err := stub.GetState(deleteLevelKey)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	lev,_ := strconv.Atoi(string(deleteLevelbytes))
	userbytes, err := stub.GetState(username)
	if err != nil {
		return shim.Error("Failed to get state")
	}
	if userbytes == nil {
		status = false
		message = '该用户不存在'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	user := User{}
	err  = json.Unmarshal(userbytes, &user)

	if user.Level < lev {
		status = false
		message = '删除权限不足'
		data[0] = strconv.FormatBool(status)
		data[1] = message
		databytes,_ := json.Marshal(data)
		return shim.Success(databytes)
	}

	// Delete the key from the state in ledger
	key, err := stub.CreateCompositeKey("Goods~Logistic:", []string{goodsId, id})
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.DelState(key)
	if err != nil {
		return shim.Error(err.Error())
	}

	status = true
	message = '删除成功'
	data[0] = strconv.FormatBool(status)
	data[1] = message
	databytes,_ := json.Marshal(data)
	return shim.Success(databytes)

	return shim.Success(databytes)
}

/* 合约入口 */
func main() {
	err := shim.Start(new(TraceChaincode))
	if err != nil {
		fmt.Printf("Error starting Trace chaincode: %s", err)
	}
}
