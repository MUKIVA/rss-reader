package com.mukiva.navigation.domain

interface IDestinationsHolder {
    val startDestination: IDestination
    val destinations: Collection<IDestination>
}