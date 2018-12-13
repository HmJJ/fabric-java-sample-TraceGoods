package main
import (
	"fmt"
	"encoding/json"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type SimpleChaincode struct {

}

func main() {
	err := shim.Start(new(SimpleChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}

type Product struct {
	PlanId string //大任务ID
	Name string //大任务名
	StartDate string //开始时间
	EndDate string //结束时间
	//Date string 
}

type Operation struct {
	TaskId string //小任务ID
	Name string //小任务名称
	Code string //小任务代码
	StartDate string //开始时间
	EndDate string //结束时间
	PlanId string //大任务ID
}

type Image struct {
	ImgId string //图片ID
	ImgUrl string //图片路径
	TaskId string //小任务ID
}

func (t *SimpleChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	return shim.Success(nil)
}

func (t *SimpleChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	function, args := stub.GetFunctionAndParameters()
	if function == "addProduct" {
		return t.addProduct(stub, args)
	} else if function == "addOperation" {
		return t.addOperation(stub, args) 
	} else if function == "modifyOperation" {
		return t.modifyOperation(stub, args)
	} else if function == "query" {
		return t.query(stub, args)
	} else if function == "queryHistory" {
		return t.queryHistory(stub, args)
	} else if function == "addImage" {
		return t.addImage(stub, args)
	} else if function == "queryImage" {
		return t.queryImage(stub, args)
	}
	return shim.Error("Received unknown function invocation")
}

func (t *SimpleChaincode) addProduct(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	if len(args) != 4 {
		return shim.Error("Incorrect number of arguments. Execting 4")
	}
	product := Product{PlanId:args[0], Name:args[1], StartDate:args[2], EndDate:args[3]}
	key := "Pro:" + args[0]
	proJsonBytes, err := json.Marshal(product)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(key, proJsonBytes)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}

func (t *SimpleChaincode) addOperation(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 6 {
		return shim.Error("Incorrect number of arguments. Execting 9")
	}
	operation := Operation{TaskId:args[0], Name:args[1], Code:args[2], StartDate:args[3], EndDate:args[4], PlanId:args[5]}
	key, err := stub.CreateCompositeKey("Operat~Pro:", []string{args[5], args[0]})
	if err != nil {
		return shim.Error(err.Error())
	}
	operatJsonBytes, err := json.Marshal(operation)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(key, operatJsonBytes)
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(nil)
}

func (t *SimpleChaincode) addImage(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 3 {
		return shim.Error("Incorrect number of arguments. Execting 3")
	}
	image := Image{ImgId:args[0], ImgUrl:args[1], TaskId:args[2]}
	key, err := stub.CreateCompositeKey("Img~Operat:", []string{args[2], args[0]})
	if err != nil {
		return shim.Error(err.Error())
	}
	imageBytes, err := json.Marshal(image)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(key, imageBytes)
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(nil)
}

func (t *SimpleChaincode) modifyOperation(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	return t.addOperation(stub, args)
}

func (t *SimpleChaincode) query(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Execting 1")
	}
	opertaHistoryMap := []map[string]interface{}{}
	resultIterator, err := stub.GetStateByPartialCompositeKey("Operat~Pro:", []string{args[0]})
	defer resultIterator.Close()
	for resultIterator.HasNext() {
		item, _ := resultIterator.Next()
		fmt.Printf("key=%s\n", item.Key)
		operatJsonBytes, err := stub.GetState(item.Key)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		operation := Operation{}
	   	err  = json.Unmarshal(operatJsonBytes, &operation)
		if err != nil {
   			return shim.Error(err.Error())
   		}


		o := map[string]interface{}{"operation":operation}
	    opertaHistoryMap = append(opertaHistoryMap, o)
	}
	opertaHistoryJson, err := json.Marshal(opertaHistoryMap)
	if err != nil {
		shim.Error("Failed to decode json of productMap")
	}
	return shim.Success([]byte(opertaHistoryJson))
}

func (t *SimpleChaincode) queryImage(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Execting 1")
	}
	imageMap := []map[string]interface{}{}
	resultIterator, err := stub.GetStateByPartialCompositeKey("Img~Operat:", []string{args[0]})
	defer resultIterator.Close()
	for resultIterator.HasNext() {
		item, _ := resultIterator.Next()
		fmt.Printf("key=%s\n", item.Key)
		imageJsonBytes, err := stub.GetState(item.Key)
		if err != nil {
			return shim.Error("Failed to get state")
		}
		image := Image{}
	   	err  = json.Unmarshal(imageJsonBytes, &image)
		if err != nil {
   			return shim.Error(err.Error())
   		}
   		
   		
		o := map[string]interface{}{"image":image}
	    imageMap = append(imageMap, o)
	}
	imageJson, err := json.Marshal(imageMap)
	if err != nil {
		shim.Error("Failed to decode json of productMap")
	}
	return shim.Success([]byte(imageJson))
}

//查询该批次号的所有历史记录
func (t *SimpleChaincode) queryHistory(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Execting 1")
	}
	opertaHistoryMap := []map[string]interface{}{}
	resultIterator, err := stub.GetStateByPartialCompositeKey("Operat~Pro:", []string{args[0]})
	defer resultIterator.Close()
	for resultIterator.HasNext() {
		item,_ := resultIterator.Next()
		it,err := stub.GetHistoryForKey(item.Key)
	   if err !=nil {
	      return shim.Error(err.Error())
	   }
	   defer it.Close()
	   for it.HasNext() {
	   		queryResponse,err := it.Next()
			if err != nil {
	   			return shim.Error(err.Error())
	   		}
	   		operation := Operation{}
		   	err  = json.Unmarshal(queryResponse.Value, &operation)
			if err != nil {
	   			return shim.Error(err.Error())
	   		}
			o := map[string]interface{}{"tx_id":queryResponse.TxId, "value":operation, "timestamp":queryResponse.Timestamp}
		    opertaHistoryMap = append(opertaHistoryMap, o)
	   }
	}
	opertaHistoryJson, err := json.Marshal(opertaHistoryMap)
	if err != nil {
		shim.Error("Failed to decode json of productMap")
	}
	return shim.Success([]byte(opertaHistoryJson))
}
