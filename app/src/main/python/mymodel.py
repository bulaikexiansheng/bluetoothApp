from torch import nn



class LSTMLayer(nn.Module):
    def __init__(self, input_size, hidden_size, num_layers):
        super(LSTMLayer, self).__init__()
        self.num_layers = num_layers
        self.hidden_size = hidden_size

        self.lstm = nn.LSTM(
            input_size = input_size,
            hidden_size = hidden_size,
            num_layers = num_layers,
            batch_first = True,
        )

    # x = (batch size, sequence length, input_size)
    # hidden = (batch size, sequence length, hidden_size)
    # hn = (num_layers, batch size, hidden_size)
    # output = (batch size, hidden_size)
    def forward(self, x):
        output, (h_n, c_n) = self.lstm(x, None)
        result = h_n[self.num_layers - 1, :, :]
        return result


class GRULayer(nn.Module):
    def __init__(self, input_size, hidden_size, num_layers):
        super(GRULayer, self).__init__()
        self.num_layers = num_layers
        self.hidden_size = hidden_size

        self.gru = nn.GRU(
            input_size = input_size,
            hidden_size = hidden_size,
            num_layers = num_layers,
            batch_first = True,
        )


    # x = (batch size, sequence length, input_size)
    # hidden = (batch size, sequence length, hidden_size)
    # hn = (num_layers, batch size, hidden_size)
    # output = (batch size, hidden_size)
    def forward(self, x):
        output, h_n = self.gru(x, None)
        result = h_n[self.num_layers - 1, :, :]

        return result


class MyModel(nn.Module):
    def __init__(self, input_size=3, hidden_size=128, num_layers=2, num_classes=9):
        super(MyModel, self).__init__()
        self.lstm = LSTMLayer(input_size=input_size, hidden_size=hidden_size, num_layers=num_layers)
        self.gru = GRULayer(input_size=input_size, hidden_size=hidden_size, num_layers=num_layers)
        self.fc = nn.Linear(self.lstm.hidden_size, num_classes)

    # x = (batch size, sequence length, input_size)
    def forward(self, x):
        # lstm_out = self.lstm(x)
        # fc_out = self.fc(lstm_out)

        gru_out = self.gru(x)
        fc_out = self.fc(gru_out)

        return fc_out


