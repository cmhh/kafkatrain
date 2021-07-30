package org.cmhh

import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer, ConvolutionLayer, SubsamplingLayer}
import org.nd4j.linalg.activations.Activation
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.learning.config.{Nesterovs, Sgd, Adam}
import org.nd4j.linalg.lossfunctions.LossFunctions 
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.conf.inputs.InputType
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.linalg.factory.Nd4j

object model {
  def ann(seed: Int = 1234): MultiLayerNetwork = {
    val conf = new NeuralNetConfiguration.Builder()
      .seed(seed)
      .updater(new Adam())
      .l2(1e-4)
      .list()
      .layer(new DenseLayer.Builder()
        .nIn(28 * 28) 
        .nOut(1000) 
        .activation(Activation.RELU) 
        .weightInit(WeightInit.XAVIER) 
        .build()
      )
      .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
        .nIn(1000)
        .nOut(10)
        .activation(Activation.SOFTMAX)
        .weightInit(WeightInit.XAVIER)
        .build()
      )
      .build()

    val network = new MultiLayerNetwork(conf)
    network.init()
    network
  }

  def cnn(learningRate: Double = 0.01, seed: Int = 1234): MultiLayerNetwork = {
    val architecture = new NeuralNetConfiguration.Builder()
      .seed(seed)
      .updater(new Sgd(learningRate)) 
      .weightInit(WeightInit.XAVIER)
      .l2(1e-4)
      .list()
      .layer(0, new ConvolutionLayer.Builder(3, 3)
        .stride(2,2)
        .padding(1,1)
        .nOut(32)
        .activation(Activation.RELU)
        .build()
      )
      .layer(1, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
        .kernelSize(2,2)
        .stride(1,1)
        .build()
      )
      .layer(2, new DenseLayer.Builder()
        .nOut(100)
        .activation(Activation.RELU)
        .build()
      )
      .layer(3, new OutputLayer.Builder(LossFunctions.LossFunction.MCXENT)
        .nOut(10)
        .activation(Activation.SOFTMAX)
        .build()
      )
      .setInputType(InputType.convolutionalFlat(28, 28, 1))
      .build
  
    val network = new MultiLayerNetwork(architecture)
    network.init()
    network
  }
}