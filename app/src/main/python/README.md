## 一、依赖库

- numpy
- pandas
- torch
- scipy
- re
- pykalman



## 二、使用说明

调用Deployment.py中的predict函数：

```python
def predict(sequence):
    
    return label
```

其中，sequence为二维数组，第一维是数据长度（20），第二维是通道数（3）；label是预测类别，以数值表示。



## 三、数据集

test_data/txt 文件夹中存在6个数据以供使用，预测结果如下：

- 0	label:2
- 1	label:3
- 2	label:1
- 3	无结果
- 4	label:7
- 5	label:8



